package com.sl.shenmian.module.offline.model;

/**
 * @Description: java类作用描述
 * @Author: qflbai
 * @CreateDate: 2019/1/12 13:36
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/12 13:36
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public enum SealType {
    /**
     * 通关施封
     */
    tongGuanPadlock(1),
    /**
     * 仓库入库解封
     */
    houseEnterDisassemble(2),
    /**
     * 仓库出库解封
     */
    houseOutPadlock(3),
    /**
     * 门店入库解封
     */
    shopDisassemble(4);

    private SealType(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return this.value;
    }

}
