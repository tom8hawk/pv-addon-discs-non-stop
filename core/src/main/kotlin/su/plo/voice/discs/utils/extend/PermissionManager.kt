package su.plo.voice.discs.utils.extend

import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import su.plo.slib.api.permission.PermissionDefault
import su.plo.slib.api.permission.PermissionManager

fun PermissionManager.registerBukkit(name: String, permissionDefault: PermissionDefault) {
    register(name, permissionDefault)
    val pluginManager = Bukkit.getPluginManager()
    if (pluginManager.getPermission(name) != null) {
        pluginManager.removePermission(name)
    }

    pluginManager.addPermission(
        Permission(
            name,
            org.bukkit.permissions.PermissionDefault.valueOf(permissionDefault.name)
        )
    )
}