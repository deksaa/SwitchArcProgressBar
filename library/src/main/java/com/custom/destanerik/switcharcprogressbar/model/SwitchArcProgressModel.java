package com.custom.destanerik.switcharcprogressbar.model;

public class SwitchArcProgressModel
{
    private String progressText;
    private String prefix;
    private String suffix;
    private String indicantText;

    public SwitchArcProgressModel(String progressText, String prefix, String suffix, String indicantText)
    {
        this.progressText = progressText;
        this.prefix = prefix;
        this.suffix = suffix;
        this.indicantText = indicantText;
    }

    public String getProgressText() {
        return progressText;
    }

    public void setProgressText(String progressText) {
        this.progressText = progressText;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getIndicantText() {
        return indicantText;
    }

    public void setIndicantText(String indicantText) {
        this.indicantText = indicantText;
    }
}
