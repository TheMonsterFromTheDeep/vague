package vague.editor.settings;

public class ToolSettings {
    public ToolSetting[] settings;
    
    public ToolSettings() {
        settings = new ToolSetting[0];
    }
    
    public ToolSettings(ToolSetting[] settings) {
        this.settings = settings;
    }
    
    public ToolSetting getSetting(String name) {
        for(ToolSetting t : settings) {
            if(t.name.equals(name)) {
                return t;
            }
        }
        return null;
    }
}