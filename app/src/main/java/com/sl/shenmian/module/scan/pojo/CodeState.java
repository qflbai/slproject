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
    /**
     * 0: 通关施封 仓库入库解封
     * 1: 仓库出库施封  门店入库解封
     */
    private int logType;

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

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }
}
