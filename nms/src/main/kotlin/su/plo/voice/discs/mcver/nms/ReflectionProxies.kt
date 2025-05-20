package su.plo.voice.discs.mcver.nms

import xyz.jpenilla.reflectionremapper.ReflectionRemapper
import xyz.jpenilla.reflectionremapper.proxy.ReflectionProxyFactory

object ReflectionProxies {

    val itemStack: ItemStackProxy
    val compoundTag: CompoundTagProxy
    
    //? if >=1.20.6 {
    /*val holder: HolderProxy
    val instrument: InstrumentProxy
    val soundEvents: SoundEventsProxy
    val dataComponents: DataComponentsProxy
    *///?}

    //? if >=1.21.3
    /*val component: ComponentProxy*/

    //? if >=1.21.5
    /*val instrumentComponent: InstrumentComponentProxy*/

    init {
        val remapper = ReflectionRemapper.forReobfMappingsInPaperJar()
        val proxyFactory = ReflectionProxyFactory.create(remapper, javaClass.classLoader)

        itemStack = proxyFactory.reflectionProxy(ItemStackProxy::class.java)
        compoundTag = proxyFactory.reflectionProxy(CompoundTagProxy::class.java)
        
        //? if >=1.20.6 {
        /*holder = proxyFactory.reflectionProxy(HolderProxy::class.java)
        instrument = proxyFactory.reflectionProxy(InstrumentProxy::class.java)
        soundEvents = proxyFactory.reflectionProxy(SoundEventsProxy::class.java)
        dataComponents = proxyFactory.reflectionProxy(DataComponentsProxy::class.java)
        *///?}

        //? if >=1.21.3
        /*component = proxyFactory.reflectionProxy(ComponentProxy::class.java)*/

        //? if >=1.21.5
        /*instrumentComponent = proxyFactory.reflectionProxy(InstrumentComponentProxy::class.java)*/
    }
}
