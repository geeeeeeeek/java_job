<template>
  <div class="content-list">
<!--    <div class="list-title">岗位管理</div>-->
    <div>
      <!--页面区域-->
      <div class="page-view">
        <div class="table-operations">
          <a-space>
            <a-button type="primary" @click="handleAdd">新增岗位</a-button>
          </a-space>
        </div>
        <a-table
            size="middle"
            rowKey="id"
            :loading="data.loading"
            :columns="columns"
            :data-source="data.dataList"
            :scroll="{ x: 'max-content' }"
            :pagination="{
          size: 'default',
          current: data.page,
          pageSize: data.pageSize,
          onChange: (current) => (data.page = current),
          showSizeChanger: false,
          showTotal: (total) => `共${total}条数据`,
        }"
        >
          <template #bodyCell="{ text, record, index, column }">
            <template v-if="column.key === 'operation'">
            <span>
              <a @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm title="确定删除?" ok-text="是" cancel-text="否" @confirm="confirmDelete(record)">
                <a href="#">删除</a>
              </a-popconfirm>
            </span>
            </template>
          </template>
        </a-table>
      </div>

      <!--弹窗区域-->
      <div>
        <a-modal
            :visible="modal.visile"
            :forceRender="true"
            :title="modal.title"
            width="880px"
            ok-text="确认"
            cancel-text="取消"
            @cancel="handleCancel"
            @ok="handleOk"
        >
          <div>
            <a-form ref="myform" :label-col="{ style: { width: '80px' } }" :model="modal.form" :rules="modal.rules">
              <a-row :gutter="24">
                <a-col span="24">
                  <a-form-item label="岗位名称" name="title">
                    <a-input placeholder="请输入" v-model:value="modal.form.title"></a-input>
                  </a-form-item>
                </a-col>
                <a-col span="12">
                  <a-form-item label="分类" name="classificationId">
                    <a-select placeholder="请选择"
                              allowClear
                              :options="modal.cData"
                              :field-names="{ label: 'title', value: 'id',}"
                              v-model:value="modal.form.classificationId">
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col span="12">
                  <a-form-item label="标签">
                    <a-select mode="multiple" placeholder="请选择" allowClear v-model:value="modal.form.tag">
                      <template v-for="item in modal.tagData">
                        <a-select-option :value="item.id">{{item.title}}</a-select-option>
                      </template>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col span="24">
                  <a-form-item label="岗位简介">
                    <a-textarea placeholder="请输入" v-model:value="modal.form.description"></a-textarea>
                  </a-form-item>
                </a-col>
                <a-col span="12">
                  <a-form-item label="工作地点" >
                    <a-input placeholder="请输入" v-model:value="modal.form.location"></a-input>
                  </a-form-item>
                </a-col>
                <a-col span="12">
                  <a-form-item label="薪资" name="status">
                    <a-input placeholder="请输入" v-model:value="modal.form.salary"></a-input>
                  </a-form-item>
                </a-col>
                <a-col span="12">
                  <a-form-item label="学历要求" name="status">
                    <a-select placeholder="请选择" allowClear v-model:value="modal.form.education">
                      <a-select-option key="不限" value="不限">不限</a-select-option>
                      <a-select-option key="专科" value="专科">专科</a-select-option>
                      <a-select-option key="本科" value="本科">本科</a-select-option>
                      <a-select-option key="研究生" value="研究生">研究生</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col span="12">
                  <a-form-item label="经验要求" name="status">
                    <a-select placeholder="请选择" allowClear v-model:value="modal.form.work_expe">
                      <a-select-option key="不限" value="不限">不限</a-select-option>
                      <a-select-option key="1-3年" value="1-3年">1-3年</a-select-option>
                      <a-select-option key="3-5年" value="3-5年">3-5年</a-select-option>
                      <a-select-option key="5年以上" value="5年以上">5年以上</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
<!--                <a-col span="12">-->
<!--                  <a-form-item label="状态" name="status">-->
<!--                    <a-select placeholder="请选择" allowClear v-model:value="modal.form.status">-->
<!--                      <a-select-option key="0" value="0">上架</a-select-option>-->
<!--                      <a-select-option key="1" value="1">下架</a-select-option>-->
<!--                    </a-select>-->
<!--                  </a-form-item>-->
<!--                </a-col>-->
              </a-row>
            </a-form>
          </div>
        </a-modal>
      </div>
    </div>
  </div>
</template>


<script setup lang="ts">
import { FormInstance, message, SelectProps } from 'ant-design-vue';
import { createApi, listUserThingApi, updateApi, deleteApi } from '/@/api/thing';
import { listUserCompanyApi } from '/@/api/company';
import {listApi as listClassificationApi} from '/@/api/classification'
import {listApi as listTagApi} from '/@/api/tag'
import {BASE_URL} from "/@/store/constants";
import { FileImageOutlined } from '@ant-design/icons-vue';
import {useUserStore} from "/@/store";
const userStore = useUserStore();


const columns = reactive([

  {
    title: '序号',
    dataIndex: 'index',
    key: 'index',
    width: 60
  },
  {
    title: '岗位名称',
    dataIndex: 'title',
    key: 'title'
  },
  {
    title: '公司名称',
    dataIndex: 'company_title',
    key: 'company_title'
  },
  {
    title: '薪资水平',
    dataIndex: 'salary',
    key: 'salary'
  },
  {
    title: '岗位描述',
    dataIndex: 'description',
    key: 'description',
    customRender:  ({ text, record, index, column }) => text ? text.substring(0, 10) + '...' : '--'
  },
  {
    title: '学历要求',
    dataIndex: 'education',
    key: 'education'
  },
  {
    title: '经验要求',
    dataIndex: 'work_expe',
    key: 'work_expe'
  },
  {
    title: '工作地点',
    dataIndex: 'location',
    key: 'location'
  },
  {
    title: '操作',
    dataIndex: 'action',
    key: 'operation',
    align: 'center',
    fixed: 'right',
    width: 140,
  },
]);

const beforeUpload = (file: File) => {
  // 改文件名
  const fileName = new Date().getTime().toString() + '.' + file.type.substring(6);
  const copyFile = new File([file], fileName);
  console.log(copyFile);
  modal.form.imageFile = copyFile;
  return false;
};

// 文件列表
const fileList = ref<any[]>([]);

// 页面数据
const data = reactive({
  dataList: [],
  loading: false,
  keyword: '',
  selectedRowKeys: [] as any[],
  pageSize: 10,
  page: 1,
});

// 弹窗数据源
const modal = reactive({
  visile: false,
  editFlag: false,
  title: '',
  cData: [],
  tagData: [{}],
  form: {
    id: undefined,
    title: undefined,
    classificationId: undefined,
    tag: [],
    status: undefined,
    location: undefined,
    salary: undefined,
    work_expe: undefined,
    education: undefined
  },
  rules: {
    title: [{ required: true, message: '请输入名称', trigger: 'change' }],
    classificationId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  },
});

const myform = ref<FormInstance>();

onMounted(() => {
  getDataList();
  getCDataList();
  getTagDataList();
});

const getDataList = () => {
  data.loading = true;

  let userId = userStore.user_id
  listUserThingApi({
    userId: userId,
  })
      .then((res) => {
        data.loading = false;
        console.log(res);
        res.data.forEach((item: any, index: any) => {
          item.index = index + 1;
        });
        data.dataList = res.data;
      })
      .catch((err) => {
        data.loading = false;
        console.log(err);
      });
}

const getCDataList = () => {
  listClassificationApi({}).then(res => {
    modal.cData = res.data
  })
}
const getTagDataList = ()=> {
  listTagApi({}).then(res => {
    res.data.forEach((item, index) => {
      item.index = index + 1
    })
    modal.tagData = res.data
  })
}

const onSearchChange = (e: Event) => {
  data.keyword = e?.target?.value;
  console.log(data.keyword);
};

const onSearch = () => {
  getDataList();
};

const rowSelection = ref({
  onChange: (selectedRowKeys: (string | number)[], selectedRows: DataItem[]) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
    data.selectedRowKeys = selectedRowKeys;
  },
});

const handleAdd = () => {
  resetModal();
  modal.visile = true;
  modal.editFlag = false;
  modal.title = '新增';
  // 重置
  for (const key in modal.form) {
    modal.form[key] = undefined;
  }
  modal.form.cover = undefined
};
const handleEdit = (record: any) => {
  resetModal();
  modal.visile = true;
  modal.editFlag = true;
  modal.title = '编辑';
  // 重置
  for (const key in modal.form) {
    modal.form[key] = undefined;
  }
  for (const key in record) {
    if(record[key]) {
      modal.form[key] = record[key];
    }
  }
  if(modal.form.cover) {
    modal.form.coverUrl = BASE_URL + modal.form.cover
    modal.form.cover = undefined
  }
};

const confirmDelete = (record: any) => {
  console.log('delete', record);
  deleteApi({ ids: record.id })
      .then((res) => {
        getDataList();
      })
      .catch((err) => {
        message.error(err.msg || '操作失败');
      });
};

const handleBatchDelete = () => {
  console.log(data.selectedRowKeys);
  if (data.selectedRowKeys.length <= 0) {
    console.log('hello');
    message.warn('请勾选删除项');
    return;
  }
  deleteApi({ ids: data.selectedRowKeys.join(',') })
      .then((res) => {
        message.success('删除成功');
        data.selectedRowKeys = [];
        getDataList();
      })
      .catch((err) => {
        message.error(err.msg || '操作失败');
      });
};

const handleOk = () => {
  myform.value
      ?.validate()
      .then(() => {
        const formData = new FormData();
        if(modal.editFlag) {
          formData.append('id', modal.form.id)
        }
        formData.append('title', modal.form.title)
        if (modal.form.classificationId) {
          formData.append('classificationId', modal.form.classificationId)
        }
        if (modal.form.tag) {
          modal.form.tag.forEach(function (value) {
            if(value){
              formData.append('tags[]', value)
            }
          })
        }

        let userId = userStore.user_id
        formData.append('userId', userId || '')

        formData.append('description', modal.form.description || '')

        // if (modal.form.status) {
        //   formData.append('status', modal.form.status)
        // }
        formData.append('status', '1')

        if (modal.form.location) {
          formData.append('location', modal.form.location)
        } if (modal.form.salary) {
          formData.append('salary', modal.form.salary)
        } if (modal.form.work_expe) {
          formData.append('work_expe', modal.form.work_expe)
        } if (modal.form.education) {
          formData.append('education', modal.form.education)
        }
        if (modal.editFlag) {
          updateApi(formData)
              .then((res) => {
                hideModal();
                getDataList();
              })
              .catch((err) => {
                console.log(err);
                message.error(err.msg || '操作失败');
              });
        } else {
          listUserCompanyApi({userId:userId}).then(res => {
            if(res.data.length>0){
              formData.append("companyId", res.data[0].id)
              createApi(formData)
                  .then((res) => {
                    hideModal();
                    getDataList();
                  })
                  .catch((err) => {
                    console.log(err);
                    message.error(err.msg || '操作失败');
                  });
            }else {
              message.warn("请完善公司资料")
            }

          }).catch(err=>{

          })

        }
      })
      .catch((err) => {
        console.log('不能为空');
      });
};

const handleCancel = () => {
  hideModal();
};

// 恢复表单初始状态
const resetModal = () => {
  myform.value?.resetFields();
  fileList.value = []
};

// 关闭弹窗
const hideModal = () => {
  modal.visile = false;
};
</script>

<style scoped lang="less">

.list-title {
  color: #152844;
  font-weight: 600;
  font-size: 18px;
  line-height: 24px;
  height: 24px;
  margin-bottom: 4px;
}

.page-view {
  min-height: 100%;
  background: #fff;
  padding: 24px;
  display: flex;
  flex-direction: column;
}

.table-operations {
  margin-bottom: 16px;
  text-align: right;
}

.table-operations > button {
  margin-right: 8px;
}
</style>
