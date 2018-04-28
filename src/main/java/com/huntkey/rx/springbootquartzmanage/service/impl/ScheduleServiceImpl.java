package com.huntkey.rx.springbootquartzmanage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.huntkey.rx.base.PropertyBaseEntity;
import com.huntkey.rx.commons.utils.rest.Pagination;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.edm.constant.EmployeeProperty;
import com.huntkey.rx.edm.constant.JobbaseinfoProperty;
import com.huntkey.rx.edm.constant.OrderProperty;
import com.huntkey.rx.edm.entity.EmployeeEntity;
import com.huntkey.rx.edm.entity.JobbaseinfoEntity;
import com.huntkey.rx.sceo.orm.common.model.OrmParam;
import com.huntkey.rx.sceo.orm.common.model.OrmParamEx;
import com.huntkey.rx.sceo.orm.common.type.DataVailidEnum;
import com.huntkey.rx.sceo.orm.common.type.SQLSortEnum;
import com.huntkey.rx.sceo.orm.config.DynamicDataSourceContextHolder;
import com.huntkey.rx.sceo.serviceCenter.common.model.NodeConstant;
import com.huntkey.rx.springbootquartzmanage.base.BaseOrderService;
import com.huntkey.rx.springbootquartzmanage.client.ModelerClient;
import com.huntkey.rx.springbootquartzmanage.client.ModelerProvider;
import com.huntkey.rx.springbootquartzmanage.constants.OrderStatus;
import com.huntkey.rx.springbootquartzmanage.entity.EdmJobPlanVO;
import com.huntkey.rx.springbootquartzmanage.entity.JobAndTrigger;
import com.huntkey.rx.springbootquartzmanage.exception.ApplicationException;
import com.huntkey.rx.springbootquartzmanage.service.IJobAndTriggerService;
import com.huntkey.rx.springbootquartzmanage.service.ScheduleService;
import com.huntkey.rx.springbootquartzmanage.utils.JsonUtils;
import com.huntkey.rx.springbootquartzmanage.utils.NullUtils;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl extends BaseOrderService<JobbaseinfoEntity, PropertyBaseEntity> implements ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    ModelerClient modelerClient;

    @Autowired
    ModelerProvider modelerProvider;

    @Autowired
    private IJobAndTriggerService iJobAndTriggerService;

    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;

    @Value("${druid.jdbc.jdbcUrl}")
    private String jdbcUrl;

    private String jobClassName = "com.huntkey.rx.springbootquartzmanage.service.impl.BaseJobImpl";

    @Override
    public void doVaildLogic(List<String> errMsgList, JobbaseinfoEntity entity) {

    }

    /**
     * @param params
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 验证单据（任务）英文名是否重复
     * @method doVaild
     */
    @Override
    public Result doVaild(JSONObject params) {

        String json = params.toJSONString();
        Object paramsObj = JSONObject.parse(json);
        // 驼峰转下划线
        JsonUtils.camel2UnderLine(paramsObj);
        JobbaseinfoEntity entity = JSONObject.parseObject(JSONObject.toJSONString(paramsObj), JobbaseinfoEntity.class);
        DynamicDataSourceContextHolder.clearDataSourceType();
        DynamicDataSourceContextHolder.setDataSourceType("edmdb");
        Result result = new Result();
        OrmParam ormParam = new OrmParam();
        ormParam.setWhereExp(OrmParam.and(ormParam.getEqualXML(JobbaseinfoProperty.JBFO_TYPE, entity.getJbfo_type()),
                ormParam.getEqualXML(JobbaseinfoProperty.JBFO_ENG_NAME, entity.getJbfo_eng_name())));
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            list = ormService.selectMapList(JobbaseinfoEntity.class, ormParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CollectionUtils.isNotEmpty(list)) {
            result.setRetCode(Result.RECODE_VALIDATE_ERROR);
            result.setErrMsg("方法名重名，请修改方法名！");
            return result;
        }
        result.setRetCode(Result.RECODE_SUCCESS);
        return result;
    }

    @Override
    public String getOrderNbrTempPrefix() {
        return "JP00";
    }

    @Override
    public String getOrderNbrPrefix() {
        return "JP00";
    }

    @Override
    public String getEntityEdmClassName() {
        return "jobbaseinfo";
    }

    @Override
    public List<PropertyBaseEntity> getEntitySetList(JobbaseinfoEntity entity) {
        return null;
    }


    /**
     * @param orderId
     * @return com.alibaba.fastjson.JSONObject
     * @description 加载单据基本信息
     * @method loadOrderEntityData
     */
    @Override
    public JSONObject loadOrderEntityData(String orderId) throws Exception {

        OrmParam ormParam = new OrmParam();
        ormParam.addColumn(JobbaseinfoProperty.JBFO_NAME)
                .addColumn(JobbaseinfoProperty.JBFO_ENG_NAME)
                .addColumn(JobbaseinfoProperty.JBFO_TYPE)
                .addColumn(JobbaseinfoProperty.JBFO_DESCRIBE)
                .addColumn(JobbaseinfoProperty.JBFO_GROUP_NAME)
                .addColumn(JobbaseinfoProperty.JBFO_IS_USE)
                .addColumn(JobbaseinfoProperty.JBFO_IS_PLAN)
                .addColumn(JobbaseinfoProperty.JBFO_STATUS)
                .addColumn(JobbaseinfoProperty.JBFO_EDM_ID);
        ormParam.setWhereExp(OrmParam.and(ormParam.getEqualXML(NodeConstant.ID, orderId)));
        List<Map<String, Object>> list = ormService.selectMapList(JobbaseinfoEntity.class, ormParam);
        JSONObject object = new JSONObject();
        for (Map<String, Object> map : list) {
            object.put(JobbaseinfoProperty.JBFO_NAME, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_NAME)));
            object.put(JobbaseinfoProperty.JBFO_ENG_NAME, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_ENG_NAME)));
            object.put(JobbaseinfoProperty.JBFO_TYPE, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_TYPE)));
            object.put(JobbaseinfoProperty.JBFO_DESCRIBE, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_DESCRIBE)));
            object.put(JobbaseinfoProperty.JBFO_GROUP_NAME, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_GROUP_NAME)));
            object.put(JobbaseinfoProperty.JBFO_IS_USE, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_IS_USE)));
            object.put(JobbaseinfoProperty.JBFO_IS_PLAN, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_IS_PLAN)));
            object.put(JobbaseinfoProperty.JBFO_STATUS, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_STATUS)));
            object.put(JobbaseinfoProperty.JBFO_EDM_ID, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_EDM_ID)));
        }
        return object;
    }

    @Override
    public JSONObject loadOrderEntitySetData(String orderId) throws Exception {
        return null;
    }

    /**
     * @param orderInstanceId
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 单据审批
     * @method handlePassLogic
     */
    @Override
    protected Result handlePassLogic(String orderInstanceId) throws Exception {

        DynamicDataSourceContextHolder.clearDataSourceType();
        DynamicDataSourceContextHolder.setDataSourceType("edmdb");
        OrmParam ormParam = new OrmParam();
        ormParam.addColumn(JobbaseinfoProperty.JBFO_EDM_ID)
                .addColumn(JobbaseinfoProperty.JBFO_NAME)
                .addColumn(JobbaseinfoProperty.JBFO_STATUS);
        ormParam.setWhereExp(OrmParam.and(ormParam.getEqualXML(NodeConstant.ID, orderInstanceId)));
        List<Map<String, Object>> list = ormService.selectMapList(JobbaseinfoEntity.class, ormParam);
        String edmId = null;
        String jbfoName = null;
        String jbfoStatus = null;
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, Object> map = list.get(0);
            edmId = (String) map.get(JobbaseinfoProperty.JBFO_EDM_ID);
            jbfoName = (String) map.get(JobbaseinfoProperty.JBFO_NAME);
            jbfoStatus = (String) map.get(JobbaseinfoProperty.JBFO_STATUS);
            if (Objects.equals(jbfoStatus, "3")) {
                remove(orderInstanceId);
                modelerClient.delAndRecover(edmId, "1");
                return updateOrderStatus(orderInstanceId, OrderStatus.ORDE_STATUS_5);
            }
            modelerClient.delAndRecover(edmId, "0");
        }

//        Result methodById = modelerClient.getMethodById(edmId);
//        Map methoddate = (Map) methodById.getData();
//        LinkedHashMap methodShow = (LinkedHashMap) methoddate.get("edmMethod_show");
//        String edmmName = (String) methodShow.get("edmmName");
//        if (!Objects.equals(edmmName, jbfoName)) {
        JobbaseinfoEntity entity = new JobbaseinfoEntity();
        entity.setId(orderInstanceId);
//            entity.setJbfo_name(edmmName);
        ormService.updateSelective(entity);
//        }
        return updateOrderStatus(orderInstanceId, OrderStatus.ORDE_STATUS_5);
    }

    @Override
    public String getAuditVaildParamAndUpdateErrorMsg(JSONObject ordeParamObj) throws Exception {
        return null;
    }


    /**
     * @param params
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 分页查询单据信息
     * @method queryOrderList
     */
    @Override
    public Result queryOrderList(JSONObject params) {

        Result result = new Result();
        OrmParamEx queryParam = new OrmParamEx();
        try {
            String columnId = OrmParamEx.column(JobbaseinfoEntity.class, NodeConstant.ID);
            String modtime = OrmParamEx.column(JobbaseinfoEntity.class, "modtime");
            String moduser = OrmParamEx.column(JobbaseinfoEntity.class, "moduser");
            queryParam.addColumn(JobbaseinfoProperty.JBFO_NAME)
                    .addColumn(JobbaseinfoProperty.JBFO_ENG_NAME)
                    .addColumn(JobbaseinfoProperty.JBFO_DESCRIBE)
                    .addColumn(OrderProperty.ORDE_STATUS)
                    .addColumn(JobbaseinfoProperty.JBFO_TYPE)
                    .addColumn(JobbaseinfoProperty.JBFO_IS_PLAN)
                    .addColumn(JobbaseinfoProperty.JBFO_IS_USE)
                    .addColumn(JobbaseinfoProperty.JBFO_STATUS)
                    .addColumn(modtime)
                    .addColumn(moduser)
                    .addColumn(EmployeeProperty.REMP_NAME)
                    .addColumn(columnId)
                    .addColumn(JobbaseinfoProperty.JBFO_GROUP_NAME)
                    .addColumn("jbfo_edm_id");

            queryParam.leftJoin(EmployeeEntity.class, OrmParamEx.joinLinkInDifferentTable
                    (EmployeeEntity.class, NodeConstant.ID, JobbaseinfoEntity.class, "moduser"), DataVailidEnum.NOMATTER);
            List<String> conditions = new ArrayList<>();
            conditions.add(queryParam.getEqualXML(OrmParamEx.column(JobbaseinfoEntity.class, "is_del"), 0));
            //任务类型
            String jobType = NullUtils.valueOf(params.get("jbfoType"));
            if (!StringUtil.isNullOrEmpty(jobType)) {
                conditions.add(queryParam.getEqualXML(JobbaseinfoProperty.JBFO_TYPE, jobType));
            }
            //任务名称
            String name = NullUtils.valueOf(params.getString("jbfoName"));
            if (!StringUtil.isNullOrEmpty(name)) {
                conditions.add(OrmParamEx.or(queryParam.getMatchMiddleXML(JobbaseinfoProperty.JBFO_ENG_NAME, name), queryParam.getMatchMiddleXML(JobbaseinfoProperty.JBFO_NAME, name)));
            }
            //任务状态
            String status = NullUtils.valueOf(params.getString("ordeStatus"));
            if (!StringUtil.isNullOrEmpty(status)) {
                conditions.add(OrmParamEx.and(queryParam.getEqualXML(OrderProperty.ORDE_STATUS, status)));
            }
            String isUse = NullUtils.valueOf(params.getString("isUse"));
            if (!StringUtil.isNullOrEmpty(isUse)) {
                conditions.add(OrmParamEx.and(queryParam.getEqualXML(JobbaseinfoProperty.JBFO_IS_USE, isUse)));
            }
//            conditions.add(OrmParamEx.and(queryParam.getEqualXML(JobbaseinfoProperty.JBFO_STATUS, "0")));
            String[] conditionArray = conditions.toArray(new String[0]);
            queryParam.setWhereExp(OrmParamEx.and(conditionArray));
            queryParam.setOrderExp(SQLSortEnum.DESC, OrderProperty.ORDE_DATE);
            int page = 1;
            int rows = 10;
            String pageStr = NullUtils.valueOf(params.get("pageNum"));
            if (!StringUtil.isNullOrEmpty(pageStr)) {
                page = Integer.parseInt(pageStr);
                if (page < 1) {
                    page = 1;
                }
            }
            String rowStr = NullUtils.valueOf(params.get("pageSize"));
            if (!StringUtil.isNullOrEmpty(rowStr)) {
                rows = Integer.parseInt(rowStr);
                if (rows < 1) {
                    rows = 10;
                }
            }
            PageHelper.startPage(page, rows, false);
            DynamicDataSourceContextHolder.clearDataSourceType();
            DynamicDataSourceContextHolder.setDataSourceType("edmdb");
            List<Map<String, Object>> list = ormService.selectMapListEx(JobbaseinfoEntity.class, queryParam);
            long totalSize = ormService.countEx(JobbaseinfoEntity.class, queryParam);
            JSONObject returnData = new JSONObject();
            JSONArray array = new JSONArray();
            for (Map<String, Object> map : list) {
                JSONObject data = new JSONObject();
                data.put(NodeConstant.ID, NullUtils.valueOf(map.get(columnId.replaceAll("\\.", "_"))));
                //制单人
                data.put("moduser", NullUtils.valueOf(map.get(EmployeeProperty.REMP_NAME)));
                data.put(JobbaseinfoProperty.JBFO_NAME, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_NAME)));
                data.put(JobbaseinfoProperty.JBFO_ENG_NAME, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_ENG_NAME)));
                data.put(JobbaseinfoProperty.JBFO_DESCRIBE, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_DESCRIBE)));
                data.put(OrderProperty.ORDE_STATUS, NullUtils.valueOf(map.get(OrderProperty.ORDE_STATUS)));
                data.put(JobbaseinfoProperty.JBFO_TYPE, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_TYPE)));
                data.put(JobbaseinfoProperty.JBFO_IS_PLAN, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_IS_PLAN)));
                data.put(JobbaseinfoProperty.JBFO_IS_USE, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_IS_USE)));
                data.put("modtime", NullUtils.valueOf(map.get(modtime.replaceAll("\\.", "_"))));
                data.put(JobbaseinfoProperty.JBFO_EDM_ID, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_EDM_ID)));
                data.put(JobbaseinfoProperty.JBFO_STATUS, NullUtils.valueOf(map.get(JobbaseinfoProperty.JBFO_STATUS)));
//                Map<String, Object> jobPlanData = new HashMap<>();
//                if (!StringUtil.isNullOrEmpty(map.get(JobbaseinfoProperty.JBFO_EDM_ID))) {
//                    String jbfoEdmId = (String) map.get(JobbaseinfoProperty.JBFO_EDM_ID);
//                    Result methodById = modelerClient.getMethodById(jbfoEdmId);
//                    Map methoddate = (Map) methodById.getData();
//                    Map edmMethodShow = (Map) methoddate.get("edmMethod_show");
//                    String edmmPlanId = (String) edmMethodShow.get("edmmPlanId");
//                    Result jobPlan = modelerClient.getJobPlan(edmmPlanId);
//                    jobPlanData = (Map) jobPlan.getData();
//                }
//                String triggerState = null;
//                if (!StringUtil.isNullOrEmpty(map.get(JobbaseinfoProperty.JBFO_GROUP_NAME))) {
//                    String groupName = (String) map.get(JobbaseinfoProperty.JBFO_GROUP_NAME);
//
////                    int i = jdbcUrl.lastIndexOf("/");
////                    int i1;
////                    String quartzName;
////                    if (jdbcUrl.contains("?")) {
////                        i1 = jdbcUrl.indexOf("?");
////                        quartzName = jdbcUrl.substring(i + 1, i1);
////                    } else {
////                        quartzName = jdbcUrl.substring(i + 1);
////                    }
//                    String sql = "select TRIGGER_STATE from biz_quartz.qrtz_triggers where JOB_GROUP = " + "'" + groupName + "'";
//                    List<Map<String, Object>> dataBySql = ormService.getDataBySql(sql);
//                    if (CollectionUtils.isNotEmpty(dataBySql)) {
//                        Map<String, Object> gourpNameMap = dataBySql.get(0);
//                        triggerState = (String) gourpNameMap.get("TRIGGER_STATE");
//                    }
//                }
//                data.put("triggerState", NullUtils.valueOf(triggerState));
//                data.put("jobPlanStartTime", NullUtils.valueOf(jobPlanData.get("jobPlanStartDate")));
//                data.put("jobPlanEndTime", NullUtils.valueOf(jobPlanData.get("jobPlanEndDate")));
                array.add(data);
            }
            returnData.put("total", totalSize);
            returnData.put("list", array);
            returnData.put("pageNum", page);
            returnData.put("pageSize", rows);
            JsonUtils.underLine2Camel(returnData);
            result.setErrMsg("查询数据成功");
            result.setData(returnData);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(Result.RECODE_ERROR, e.getMessage());
        }
    }

    /**
     * @param id
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 删除接口，删除时要删除单据信息、单据下的方法信息、该方法所包含的计划信息
     * @method remove
     */
    @Override
    public Result remove(String id) throws Exception {

        Result result = new Result();
        OrmParam ormParam = new OrmParam();
        DynamicDataSourceContextHolder.clearDataSourceType();
        DynamicDataSourceContextHolder.setDataSourceType("edmdb");
        ormParam.addColumn(OrderProperty.ORDE_STATUS)
                .addColumn(JobbaseinfoProperty.JBFO_EDM_ID);
        ormParam.setWhereExp(OrmParam.and(ormParam.getEqualXML(NodeConstant.ID, id)));
        List<Map<String, Object>> list = ormService.selectMapList(JobbaseinfoEntity.class, ormParam);
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, Object> map = list.get(0);
            //清除数据源，回到本地数据库
            DynamicDataSourceContextHolder.clearDataSourceType();
            //获取到方法Id
            String edmmId = (String) map.get(JobbaseinfoProperty.JBFO_EDM_ID);
            //根据方法Id获取该方法下的计划信息
            Result jobPlanResult = modelerClient.getAllPlansByMethodId(edmmId);
            //遍历查询到的计划列表，在遍历过程中把该方法所包含的计划信息删除
            if (result.getData() != null) {
                JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(jobPlanResult.getData()));
                List<EdmJobPlanVO> edmJobPlanVOList = jsonArray.stream().map(obj -> JSONObject.toJavaObject((JSONObject) JSON.toJSON(obj), EdmJobPlanVO.class)).collect(Collectors.toList());
                if (edmJobPlanVOList.size() > 0) {
                    for (EdmJobPlanVO edmJobPlanVo : edmJobPlanVOList
                            ) {
                        //删除计划表里的计划信息和quartz表里面的计划信息
                        deletePlanById(edmJobPlanVo.getJobPlanId());
                    }
                }
            }
            //删除单据下的EDM方法信息
            modelerProvider.delete(edmmId);
            //重新切换数据源到edmdb中
            DynamicDataSourceContextHolder.setDataSourceType("edmdb");
            //删除单据信息
            ormService.delete(JobbaseinfoEntity.class, id);
            result.setData("删除计划成功！");
            return result;

        }
        result.setErrMsg("删除计划失败！");
        result.setRetCode(Result.RECODE_ERROR);
        return result;
    }

    /**
     * @param methodId
     * @param pageNum
     * @param pageSize
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 根据方法Id查询方法所包含的计划信息，并将计划的执行情况也放到分页列表里
     * @method selectPlanByMethodId
     */
    @Override
    public Result selectPlanByMethodId(String methodId, int pageNum, int pageSize) {
        DynamicDataSourceContextHolder.clearDataSourceType();
        Result result;
        try {
            //先从EDM那边查到该方法Id所包含的计划信息
            result = modelerClient.planByMethodId(methodId, pageNum, pageSize);
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(result.getData()));
            if (jsonObject != null) {
                List<JSONObject> lsit = (List<JSONObject>) jsonObject.get("list");
                JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(lsit));
                List<EdmJobPlanVO> voList = jsonArray.stream().map(obj -> JSONObject.toJavaObject((JSONObject) JSON.toJSON(obj), EdmJobPlanVO.class)).collect(Collectors.toList());

                if (voList != null && voList.size() > 0) {
                    for (EdmJobPlanVO edmJobPlanVO : voList) {
                        String planId = edmJobPlanVO.getJobPlanId();
                        String planGroupName = planId;
                        JobAndTrigger jobAndTrigger = iJobAndTriggerService.selectJobByGroupName(planGroupName);
                        if (jobAndTrigger != null) {
                            edmJobPlanVO.setTriggerState(jobAndTrigger.getTRIGGER_STATE());
                        }
                    }
                }

                long total = Long.valueOf(jsonObject.get("total").toString());
                Pagination<EdmJobPlanVO> pagination = new Pagination<EdmJobPlanVO>(voList, pageNum, pageSize, total);
                result.setData(pagination);
            }
        } catch (Exception e) {
            logger.error("selectPlanByMethodId方法执行出错", e);
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * @param planId
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 根据计划Id，删除EDM数据库，job_plan计划表里的计划信息和quartz表里面的计划信息
     * @method deletePlanById
     */
    @Override
    public Result deletePlanById(String planId) {

        Result result;
        try {
            //删除EDM计划表里面的计划信息
            result = modelerClient.jobs(planId);
            //删除quartz表里面的计划
            iJobAndTriggerService.deleteJob(planId, scheduler, jobClassName);
        } catch (Exception e) {
            logger.error("方法执行出错", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * @param jobName
     * @return java.lang.Boolean
     * @description 判断单据名是否在数据库中已存在
     * @method isJobNameExist
     */
    @Override
    public Boolean isJobNameExist(String jobName) {

        DynamicDataSourceContextHolder.clearDataSourceType();
        DynamicDataSourceContextHolder.setDataSourceType("edmdb");
        try {
            OrmParam ormParam = new OrmParam();
            ormParam.setWhereExp(ormParam.getEqualXML(JobbaseinfoProperty.JBFO_NAME, jobName));
            List<JobbaseinfoEntity> list = ormService.selectBeanList(JobbaseinfoEntity.class, ormParam);
            if (list.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 分页查询EDM中的方法信息，支持分页
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Result getAllMethods(int pageNum, int pageSize) {
        Result result = new Result();
        try {
            result = modelerProvider.getMethods(null, null, null, null, null, pageNum, pageSize);
        } catch (Exception e) {
            logger.error("getAllMethods方法执行出错", e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("getAllMethods方法执行出错");
            throw new RuntimeException(e);
        }
        return result;
    }
}
