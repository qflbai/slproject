package com.sl.shenmian.module.scan.pojo;

/**
 * 封条状态
 */
public class CodeState {
    /**
     * 封条存在状态
     * 0:不存在封条信息，1:存在封条信息
     */
    private boolean hasSeal;
    /**
     * 封条施封状态
     * 0:未施封，1:已施封
     */
    private boolean hasLocked;
    /**
     * 封条解封状态
     * 0:未解封，1:已解封
     */

    private boolean hasUnblock;

    public boolean isHasSeal() {
        return hasSeal;
    }

    public void setHasSeal(boolean hasSeal) {
        this.hasSeal = hasSeal;
    }

    public boolean isHasLocked() {
        return hasLocked;
    }

    public void setHasLocked(boolean hasLocked) {
        this.hasLocked = hasLocked;
    }

    public boolean isHasUnblock() {
        return hasUnblock;
    }

    public void setHasUnblock(boolean hasUnblock) {
        this.hasUnblock = hasUnblock;
    }

}
