package su.plo.voice.discs.mcver.nms

import xyz.jpenilla.reflectionremapper.proxy.annotation.Proxies
import xyz.jpenilla.reflectionremapper.proxy.annotation.Type

@Proxies(
    className = "net.minecraft.world.item.ItemStack"
)
interface ItemStackProxy {

    //? if >=1.20.6 {
    /*fun set(
        @Type(className = "net.minecraft.world.item.ItemStack") instance: Any,
        @Type(className = "net.minecraft.core.component.DataComponentType") dataType: Any,
        dataValue: Any
    )
    *///?} else {
    fun getOrCreateTag(
        @Type(className = "net.minecraft.world.item.ItemStack") instance: Any
    ): Any
    //?}
}