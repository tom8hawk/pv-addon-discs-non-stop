plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "1.19.4" /* [SC] DO NOT EDIT */

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) { 
    group = "project"
    ofTask("build")
}
