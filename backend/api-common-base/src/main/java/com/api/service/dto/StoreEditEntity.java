package com.api.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * mer门店信息
 */
@Data
@ApiModel(value = "StoreEditEntity")
public class StoreEditEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主表信息
     */
    @ApiModelProperty(value = "1.基础信息:必填:门店名称", dataType = "String", position = 3)
    private String storeName;

    @ApiModelProperty(value = "公司简称", dataType = "String")
    private String shortName;

    @ApiModelProperty(value = "2.基础信息:必填:经度", dataType = "String", position = 4)
    private String longitude;

    @ApiModelProperty(value = "3.基础信息:必填:纬度", dataType = "String", position = 5)
    private String latitude;

    @ApiModelProperty(value = "4.基础信息:必填:门店详细地址", dataType = "String", position = 6)
    private String address;

    @ApiModelProperty(value = "5.基础信息:必填:门店联系电话", dataType = "String", position = 7)
    private String mobile;

    @ApiModelProperty(value = "6.基础信息:必填:门店LOGO", dataType = "String", position = 8)
    private String doorPhotoUrl;


    @ApiModelProperty(value = "8.基础信息:图片集合", dataType = "String", position = 10)
    private List<String> photosUrlList;



    /**
     * 商户/门店信息
     */


    @ApiModelProperty(value = "1.主体信息:必填:营业执照类型,码值:business_license [10011001公司/企业][10011002个体工商户]", dataType = "String", position = 17)
    private String businessLicense;


    @ApiModelProperty(value = "2.主体信息:必填:营业执照图片", dataType = "String", position = 18)
    private String licensePic;

    @ApiModelProperty(value = "3.主体信息:必填:营业执照名称", dataType = "String", position = 19)
    private String licenseName;

    @ApiModelProperty(value = "4.主体信息:必填:营业执照编号", dataType = "String", position = 20)
    private String licenseCode;

    @ApiModelProperty(value = "* 5.主体信息(个体不展示):公司(企业)必填:公司开户行名称", dataType = "String", position = 21)
    private String companyBankName;

    @ApiModelProperty(value = "* 6.主体信息(个体不展示):公司(企业)必填:公司开户银行号", dataType = "String", position = 22)
    private String companyBankNo;

    @ApiModelProperty(value = "* 7.主体信息(个体不展示):公司(企业)必填:对公账户证明图片", dataType = "String", position = 23)
    private String corporatePic;

    /**
     * 商户/门店个人提现信息
     */

    @ApiModelProperty(value = "1.法人信息:必填:法人身份证正面", dataType = "String", position = 24)
    private String idCardPic1;

    @ApiModelProperty(value = "2.法人信息:必填:法人身份证背面", dataType = "String", position = 25)
    private String idCardPic2;

    @ApiModelProperty(value = "3.法人信息:必填:法人身份证名", dataType = "String", position = 26)
    private String idCardName;

    @ApiModelProperty(value = "4.法人信息:必填:法人身份证号", dataType = "String", position = 27)
    private String idCardNo;

    @ApiModelProperty(value = "5.法人信息:必填:法人银行卡名称", dataType = "String", position = 28)
    private String bankName;

    @ApiModelProperty(value = "* 6.法人信息(企业不展示):必填:法人银行卡号", dataType = "String", position = 29)
    private String bankNo;

    @ApiModelProperty(value = "* 7.法人信息(企业不展示):必填:法人银行卡照片", dataType = "String", position = 30)
    private String bankPic;

    @ApiModelProperty(value = "必填:银行卡国家代码(企业填公账开户行、个体填银行卡开户行)", dataType = "String")
    private String bankCode;





}