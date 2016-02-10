package vague.editor.settings;

public class ToolSettings {
    ToolSetting[] settings;
    
    public ToolSettings() {
        settings = new ToolSetting[0];
    }
    
    public ToolSetting getSetting(String name) {
        for(ToolSetting t : settings) {
            if(t.name.equals(name)) {
                return t;
            }
        }
        return null;
    }
    
    public void setSettings(ToolSetting[] settings) {
        this.settings = settings;
    }
}