package eu.darken.bluemusic.main.core.service.modules

import dagger.MapKey
import dagger.internal.Beta
import kotlin.reflect.KClass

@Beta
@MapKey
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class VolumeModuleKey(val value: KClass<out VolumeModule>)
