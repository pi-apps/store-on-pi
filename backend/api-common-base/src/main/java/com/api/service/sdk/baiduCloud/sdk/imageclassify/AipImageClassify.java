/*
 * Copyright 2017 Baidu, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.api.service.sdk.baiduCloud.sdk.imageclassify;

import com.api.service.sdk.baiduCloud.sdk.client.BaseClient;
import com.api.service.sdk.baiduCloud.sdk.error.AipError;
import com.api.service.sdk.baiduCloud.sdk.http.AipRequest;
import com.api.service.sdk.baiduCloud.sdk.imageclassify.ImageClassifyConsts;
import com.api.service.sdk.baiduCloud.sdk.util.Base64Util;
import com.api.service.sdk.baiduCloud.sdk.util.Util;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class AipImageClassify extends BaseClient {

    public AipImageClassify(String appId, String apiKey, String secretKey) {
        super(appId, apiKey, secretKey);
    }

    /**
     * 通用物体识别接口   
     * 该请求用于通用物体及场景识别，即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片中的多个物体及场景标签。
     *
     * @param image - 二进制图像数据
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   baike_num 返回百科信息的结果数，默认不返回
     * @return JSONObject
     */
    public JSONObject advancedGeneral(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.ADVANCED_GENERAL);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * 通用物体识别接口
     * 该请求用于通用物体及场景识别，即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片中的多个物体及场景标签。
     *
     * @param image - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   baike_num 返回百科信息的结果数，默认不返回
     * @return JSONObject
     */
    public JSONObject advancedGeneral(String image, HashMap<String, String> options) {
        try {
            byte[] data = Util.readFileByBytes(image);
            return advancedGeneral(data, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

    /**
     * 菜品识别接口   
     * 该请求用于菜品识别。即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片的菜品名称、卡路里信息、置信度。
     *
     * @param image - 二进制图像数据
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   top_num 返回预测得分top结果数，默认为5
     *   filter_threshold 默认0.95，可以通过该参数调节识别效果，降低非菜识别率.
     *   baike_num 返回百科信息的结果数，默认不返回
     * @return JSONObject
     */
    public JSONObject dishDetect(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.DISH_DETECT);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * 菜品识别接口
     * 该请求用于菜品识别。即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片的菜品名称、卡路里信息、置信度。
     *
     * @param image - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   top_num 返回预测得分top结果数，默认为5
     *   filter_threshold 默认0.95，可以通过该参数调节识别效果，降低非菜识别率.
     *   baike_num 返回百科信息的结果数，默认不返回
     * @return JSONObject
     */
    public JSONObject dishDetect(String image, HashMap<String, String> options) {
        try {
            byte[] data = Util.readFileByBytes(image);
            return dishDetect(data, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

    /**
     * 车辆识别接口   
     * 该请求用于检测一张车辆图片的具体车型。即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片的车辆品牌及型号。
     *
     * @param image - 二进制图像数据
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   top_num 返回预测得分top结果数，默认为5
     *   baike_num 返回百科信息的结果数，默认不返回
     * @return JSONObject
     */
    public JSONObject carDetect(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.CAR_DETECT);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * 车辆识别接口
     * 该请求用于检测一张车辆图片的具体车型。即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片的车辆品牌及型号。
     *
     * @param image - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   top_num 返回预测得分top结果数，默认为5
     *   baike_num 返回百科信息的结果数，默认不返回
     * @return JSONObject
     */
    public JSONObject carDetect(String image, HashMap<String, String> options) {
        try {
            byte[] data = Util.readFileByBytes(image);
            return carDetect(data, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

    /**
     * logo商标识别接口   
     * 该请求用于检测和识别图片中的品牌LOGO信息。即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片中LOGO的名称、位置和置信度。当效果欠佳时，可以建立子库（在[控制台](https://console.bce.baidu.com/ai/#/ai/imagerecognition/overview/index)创建应用并申请建库）并通过调用logo入口接口完成自定义logo入库，提高识别效果。
     *
     * @param image - 二进制图像数据
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   custom_lib 是否只使用自定义logo库的结果，默认false：返回自定义库+默认库的识别结果
     * @return JSONObject
     */
    public JSONObject logoSearch(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.LOGO_SEARCH);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * logo商标识别接口
     * 该请求用于检测和识别图片中的品牌LOGO信息。即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片中LOGO的名称、位置和置信度。当效果欠佳时，可以建立子库（在[控制台](https://console.bce.baidu.com/ai/#/ai/imagerecognition/overview/index)创建应用并申请建库）并通过调用logo入口接口完成自定义logo入库，提高识别效果。
     *
     * @param image - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   custom_lib 是否只使用自定义logo库的结果，默认false：返回自定义库+默认库的识别结果
     * @return JSONObject
     */
    public JSONObject logoSearch(String image, HashMap<String, String> options) {
        try {
            byte[] data = Util.readFileByBytes(image);
            return logoSearch(data, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

    /**
     * logo商标识别—添加接口   
     * 使用入库接口请先在[控制台](https://console.bce.baidu.com/ai/#/ai/imagerecognition/overview/index)创建应用并申请建库，建库成功后方可正常使用。
     *
     * @param image - 二进制图像数据
     * @param brief - brief，检索时带回。此处要传对应的name与code字段，name长度小于100B，code长度小于150B
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     * @return JSONObject
     */
    public JSONObject logoAdd(byte[] image, String brief, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        
        request.addBody("brief", brief);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.LOGO_ADD);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * logo商标识别—添加接口
     * 使用入库接口请先在[控制台](https://console.bce.baidu.com/ai/#/ai/imagerecognition/overview/index)创建应用并申请建库，建库成功后方可正常使用。
     *
     * @param image - 本地图片路径
     * @param brief - brief，检索时带回。此处要传对应的name与code字段，name长度小于100B，code长度小于150B
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     * @return JSONObject
     */
    public JSONObject logoAdd(String image, String brief, HashMap<String, String> options) {
        try {
            byte[] data = Util.readFileByBytes(image);
            return logoAdd(data, brief, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

    /**
     * logo商标识别—删除接口   
     * 使用删除接口请先在[控制台](https://console.bce.baidu.com/ai/#/ai/imagerecognition/overview/index)创建应用并申请建库，建库成功后先调用入库接口完成logo图片入库，删除接口用户在已入库的logo图片中删除图片。
     *
     * @param image - 二进制图像数据
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     * @return JSONObject
     */
    public JSONObject logoDeleteByImage(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.LOGO_DELETE);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * logo商标识别—删除接口
     * 使用删除接口请先在[控制台](https://console.bce.baidu.com/ai/#/ai/imagerecognition/overview/index)创建应用并申请建库，建库成功后先调用入库接口完成logo图片入库，删除接口用户在已入库的logo图片中删除图片。
     *
     * @param image - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     * @return JSONObject
     */
    public JSONObject logoDeleteByImage(String image, HashMap<String, String> options) {
        try {
            byte[] data = Util.readFileByBytes(image);
            return logoDeleteByImage(data, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

    /**
     * logo商标识别—删除接口   
     * 使用删除接口请先在[控制台](https://console.bce.baidu.com/ai/#/ai/imagerecognition/overview/index)创建应用并申请建库，建库成功后先调用入库接口完成logo图片入库，删除接口用户在已入库的logo图片中删除图片。
     *
     * @param contSign - 图片签名（和image二选一，image优先级更高）
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     * @return JSONObject
     */
    public JSONObject logoDeleteBySign(String contSign, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        request.addBody("cont_sign", contSign);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.LOGO_DELETE);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * 动物识别接口   
     * 该请求用于识别一张图片。即对于输入的一张图片（可正常解码，且长宽比适宜），输出动物识别结果
     *
     * @param image - 二进制图像数据
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   top_num 返回预测得分top结果数，默认为6
     *   baike_num 返回百科信息的结果数，默认不返回
     * @return JSONObject
     */
    public JSONObject animalDetect(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.ANIMAL_DETECT);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * 动物识别接口
     * 该请求用于识别一张图片。即对于输入的一张图片（可正常解码，且长宽比适宜），输出动物识别结果
     *
     * @param image - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   top_num 返回预测得分top结果数，默认为6
     *   baike_num 返回百科信息的结果数，默认不返回
     * @return JSONObject
     */
    public JSONObject animalDetect(String image, HashMap<String, String> options) {
        try {
            byte[] data = Util.readFileByBytes(image);
            return animalDetect(data, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

    /**
     * 植物识别接口   
     * 该请求用于识别一张图片。即对于输入的一张图片（可正常解码，且长宽比适宜），输出植物识别结果。
     *
     * @param image - 二进制图像数据
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   baike_num 返回百科信息的结果数，默认不返回
     * @return JSONObject
     */
    public JSONObject plantDetect(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.PLANT_DETECT);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * 植物识别接口
     * 该请求用于识别一张图片。即对于输入的一张图片（可正常解码，且长宽比适宜），输出植物识别结果。
     *
     * @param image - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   baike_num 返回百科信息的结果数，默认不返回
     * @return JSONObject
     */
    public JSONObject plantDetect(String image, HashMap<String, String> options) {
        try {
            byte[] data = Util.readFileByBytes(image);
            return plantDetect(data, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

    /**
     * 图像主体检测接口   
     * 用户向服务请求检测图像中的主体位置。
     *
     * @param image - 二进制图像数据
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   with_face 如果检测主体是人，主体区域是否带上人脸部分，0-不带人脸区域，其他-带人脸区域，裁剪类需求推荐带人脸，检索/识别类需求推荐不带人脸。默认取1，带人脸。
     * @return JSONObject
     */
    public JSONObject objectDetect(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.OBJECT_DETECT);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * 图像主体检测接口
     * 用户向服务请求检测图像中的主体位置。
     *
     * @param image - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   with_face 如果检测主体是人，主体区域是否带上人脸部分，0-不带人脸区域，其他-带人脸区域，裁剪类需求推荐带人脸，检索/识别类需求推荐不带人脸。默认取1，带人脸。
     * @return JSONObject
     */
    public JSONObject objectDetect(String image, HashMap<String, String> options) {
        try {
            byte[] data = Util.readFileByBytes(image);
            return objectDetect(data, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

    /**
     * 地标识别接口   
     * 该请求用于识别地标，即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片中的地标识别结果。
     *
     * @param image - 二进制图像数据
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     * @return JSONObject
     */
    public JSONObject landmark(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.LANDMARK);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * 地标识别接口
     * 该请求用于识别地标，即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片中的地标识别结果。
     *
     * @param image - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     * @return JSONObject
     */
    public JSONObject landmark(String image, HashMap<String, String> options) {
        try {
            byte[] data = Util.readFileByBytes(image);
            return landmark(data, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

    /**
     * 花卉识别接口   
     * 检测用户上传的花卉图片，输出图片的花卉识别结果名称及对应的概率打分。
     *
     * @param image - 二进制图像数据
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   top_num 返回预测得分top结果数，默认为5
     *   baike_num 返回百科信息的结果数，默认不返回
     * @return JSONObject
     */
    public JSONObject flower(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.FLOWER);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * 花卉识别接口
     * 检测用户上传的花卉图片，输出图片的花卉识别结果名称及对应的概率打分。
     *
     * @param image - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   top_num 返回预测得分top结果数，默认为5
     *   baike_num 返回百科信息的结果数，默认不返回
     * @return JSONObject
     */
    public JSONObject flower(String image, HashMap<String, String> options) {
        try {
            byte[] data = Util.readFileByBytes(image);
            return flower(data, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

    /**
     * 食材识别接口   
     * 该请求用于识别果蔬类食材，即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片中的果蔬食材结果。
     *
     * @param image - 二进制图像数据
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   top_num 返回预测得分top结果数，如果为空或小于等于0默认为5；如果大于20默认20
     * @return JSONObject
     */
    public JSONObject ingredient(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.INGREDIENT);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * 食材识别接口
     * 该请求用于识别果蔬类食材，即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片中的果蔬食材结果。
     *
     * @param image - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     *   top_num 返回预测得分top结果数，如果为空或小于等于0默认为5；如果大于20默认20
     * @return JSONObject
     */
    public JSONObject ingredient(String image, HashMap<String, String> options) {
        try {
            byte[] data = Util.readFileByBytes(image);
            return ingredient(data, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

    /**
     * 红酒识别接口   
     * 该服务用于识别红酒标签，即对于输入的一张图片（可正常解码，长宽比适宜，且酒标清晰可见），输出图片中的红酒名称、国家、产区、酒庄、类型、糖分、葡萄品种、酒品描述等信息。可识别数十万中外常见红酒。
     *
     * @param image - 二进制图像数据
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     * @return JSONObject
     */
    public JSONObject redwine(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.REDWINE);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * 红酒识别接口
     * 该服务用于识别红酒标签，即对于输入的一张图片（可正常解码，长宽比适宜，且酒标清晰可见），输出图片中的红酒名称、国家、产区、酒庄、类型、糖分、葡萄品种、酒品描述等信息。可识别数十万中外常见红酒。
     *
     * @param image - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     * @return JSONObject
     */
    public JSONObject redwine(String image, HashMap<String, String> options) {
        try {
            byte[] data = Util.readFileByBytes(image);
            return redwine(data, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

    /**
     * 货币识别接口   
     * 识别图像中的货币类型，以纸币为主，正反面均可准确识别，接口返回货币的名称、代码、面值、年份信息；可识别各类近代常见货币，如美元、欧元、英镑、法郎、澳大利亚元、俄罗斯卢布、日元、韩元、泰铢、印尼卢比等。
     *
     * @param image - 二进制图像数据
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     * @return JSONObject
     */
    public JSONObject currency(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        preOperation(request);
        
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }
        request.setUri(ImageClassifyConsts.CURRENCY);
        postOperation(request);
        return requestServer(request);
    }

    /**
     * 货币识别接口
     * 识别图像中的货币类型，以纸币为主，正反面均可准确识别，接口返回货币的名称、代码、面值、年份信息；可识别各类近代常见货币，如美元、欧元、英镑、法郎、澳大利亚元、俄罗斯卢布、日元、韩元、泰铢、印尼卢比等。
     *
     * @param image - 本地图片路径
     * @param options - 可选参数对象，key: value都为string类型
     * options - options列表:
     * @return JSONObject
     */
    public JSONObject currency(String image, HashMap<String, String> options) {
        try {
            byte[] data = Util.readFileByBytes(image);
            return currency(data, options);
        } catch (IOException e) {
            e.printStackTrace();
            return AipError.IMAGE_READ_ERROR.toJsonResult();
        }
    }

}