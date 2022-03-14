class PluginConfig {

    boolean isApply = true
    boolean useLocal
    String path
    String id

    String getGroupId() {
        String[] splits = path.split(":")
        return splits.length == 3 ? splits[0] : null
    }

    String getArtifactId() {
        String[] splits = path.split(":")
        return splits.length == 3 ? splits[1] : null
    }

    String getVersion() {
        String[] splits = path.split(":")
        return splits.length == 3 ? splits[2] : null
    }

    @Override
    String toString() {
        return "PluginConfig { isApply = ${getFlag(isApply)}" +
                ", useLocal = ${getFlag(useLocal)}" +
                ", path = " + path +
                ", id = " + id +
                " }"
    }

    static String getFlag(boolean b) {
        return b ? "✅" : "❌"
    }
}