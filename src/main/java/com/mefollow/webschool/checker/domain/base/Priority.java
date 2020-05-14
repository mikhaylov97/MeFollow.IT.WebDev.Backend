package com.mefollow.webschool.checker.domain.base;

public class Priority {
    private int siblingNumber;
    private boolean inline;
    private boolean important;

    public Priority(int siblingNumber, boolean important) {
        this.siblingNumber = siblingNumber;
        this.important = important;
    }

    public Priority(boolean inline, boolean important) {
        this.inline = inline;
        this.important = important;
    }

    public boolean isLowerThan(Priority priority) {
        return !isHigherThan(priority);
    }

    public boolean isHigherThan(Priority priority) {
        if (this.inline) {
            if (this.important) {
                // this object is important and opponent is not important => return true;
                // this object is important and opponent is too important =>
                // 										1) this object has higher priority => return true;
                // 										2) this object has lower priority => return false;
                return !priority.inline || !priority.important;
            } else {
                // this object is NOT important and opponent is important => return false;
                // this object is NOT important and opponent is too NOT important =>
                // 										1) this object has higher priority => return true;
                // 										2) this object has lower priority => return false;
                return !priority.inline || !priority.important;
            }
        } else {
            if (this.important) {
                // this object is important and opponent is not important => return true;
                // this object is important and opponent is too important =>
                // 										1) this object has higher priority => return true;
                // 										2) this object has lower priority => return false;
                return (!priority.inline && !priority.important) || this.siblingNumber > priority.siblingNumber;
            } else {
                // this object is NOT important and opponent is important => return false;
                // this object is NOT important and opponent is too NOT important =>
                // 										1) this object has higher priority => return true;
                // 										2) this object has lower priority => return false;
                return !priority.inline && !priority.important && this.siblingNumber > priority.siblingNumber;
            }
        }
    }
}