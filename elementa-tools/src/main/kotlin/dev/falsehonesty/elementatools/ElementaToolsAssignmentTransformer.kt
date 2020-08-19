package dev.falsehonesty.elementatools

import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.backend.common.lower.at
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrSetField
import org.jetbrains.kotlin.ir.expressions.IrSetVariable
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.util.isSubclassOf
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.name.FqName

class ElementaToolsAssignmentTransformer(
    private val context: IrPluginContext
) : IrElementTransformerVoidWithContext(), FileLoweringPass {
    private val targetSuperClass = context.referenceClass(TARGET_SUPER_CLASS_NAME)!!
    private var currentVariableName: String? = null

    override fun lower(irFile: IrFile) {
        irFile.transformChildrenVoid()
    }

    override fun visitConstructorCall(expression: IrConstructorCall): IrExpression {
        if (currentVariableName == null)
            return super.visitConstructorCall(expression)
        val constructorClass = expression.type.getClass() ?: return super.visitConstructorCall(expression)
        if (!constructorClass.isSubclassOf(targetSuperClass.owner))
            return super.visitConstructorCall(expression)

        val namePropertySetter = constructorClass.properties.find { it.name.asString() == "componentName" }?.setter
            ?: return super.visitConstructorCall(expression)

        val symbol = currentScope!!.scope.scopeOwnerSymbol
        return DeclarationIrBuilder(context, symbol).run {
            at(expression)

            irBlock {
                val constructedInstance = irTemporary(expression)
                +irSet(
                    namePropertySetter.returnType,
                    irGet(constructedInstance),
                    namePropertySetter.symbol,
                    irString(currentVariableName!!)
                )
                +irGet(constructedInstance)
            }
        }.also {
            currentVariableName = null
        }
    }

    override fun visitVariable(declaration: IrVariable): IrStatement {
        currentVariableName = declaration.name.asString()
        return super.visitVariable(declaration)
    }

    override fun visitSetVariable(expression: IrSetVariable): IrExpression {
        currentVariableName = expression.symbol.owner.name.asString()
        return super.visitSetVariable(expression)
    }

    override fun visitFieldNew(declaration: IrField): IrStatement {
        currentVariableName = declaration.name.asString()
        return super.visitFieldNew(declaration)
    }

    override fun visitSetField(expression: IrSetField): IrExpression {
        currentVariableName = expression.symbol.owner.name.asString()
        return super.visitSetField(expression)
    }

    companion object {
        val TARGET_SUPER_CLASS_NAME = FqName("club.sk1er.elementa.UIComponent")
    }
}
