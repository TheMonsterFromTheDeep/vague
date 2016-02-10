package vague.editor.settings;

import vague.menu.ToolOptions;

public class ToolSetting {
    public final String name; //The name of the tool settings
    
    public final ToolOptions.OptionPanel panel; //The panel that displays the tool settings
    
    public ToolSetting(String name, ToolOptions.OptionPanel panel) {
        this.name = name;
        
        this.panel = panel;
    }
}