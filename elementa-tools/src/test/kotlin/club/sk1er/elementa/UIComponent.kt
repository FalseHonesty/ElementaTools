package club.sk1er.elementa

abstract class UIComponent {
    var componentName: String? = null

    fun constrain(x: () -> Unit) = apply {

    }

    infix fun childOf(other: UIComponent) = apply {

    }
}