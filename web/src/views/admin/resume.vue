<template>
  <div>
    <!--页面区域-->
    <div class="page-view">
      <div class="table-operations">
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
          <template v-if="column.key === 'coverUrl'">
            <img :src="text" style="width: 40px;height: 40px;"/>
          </template>
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
              <a-col span="12">
                <a-form-item label="姓名" name="name">
                  <a-input placeholder="请输入" v-model:value="modal.form.name"></a-input>
                </a-form-item>
              </a-col>
              <a-col span="12">
                <a-form-item label="性别" name="sex">
                  <a-select placeholder="请选择" allowClear v-model:value="modal.form.sex">
                    <a-select-option key="男" value="男">男</a-select-option>
                    <a-select-option key="女" value="女">女</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col span="24">
                <a-form-item label="头像">
                  <a-upload-dragger
                      name="file"
                      accept="image/*"
                      :multiple="false"
                      :before-upload="beforeUpload"
                  >
                    <p class="ant-upload-text">
                      请选择图片
                    </p>
                  </a-upload-dragger>
                </a-form-item>
              </a-col>
              <a-col span="12">
                <a-form-item label="出生日期" >
                  <a-input placeholder="请输入" v-model:value="modal.form.birthday"></a-input>
                </a-form-item>
              </a-col>
              <a-col span="12">
                <a-form-item label="学历">
                  <a-select placeholder="请选择" allowClear v-model:value="modal.form.education">
                    <a-select-option key="专科" value="专科">专科</a-select-option>
                    <a-select-option key="本科" value="本科">本科</a-select-option>
                    <a-select-option key="研究生" value="研究生">研究生</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col span="12">
                <a-form-item label="毕业学校">
                  <a-input placeholder="请输入" v-model:value="modal.form.school"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </div>
      </a-modal>
    </div>
  </div>
</template>

<script setup lang="ts">
import { FormInstance, message, SelectProps } from 'ant-design-vue';
import { createApi, listApi, updateApi, deleteApi } from '/@/api/resume';
import {BASE_URL} from "/@/store/constants";
import { FileImageOutlined } from '@ant-design/icons-vue';

const columns = reactive([

  {
    title: '序号',
    dataIndex: 'index',
    key: 'index',
    width: 60
  },
  // {
  //   title: '用户名',
  //   dataIndex: 'username',
  //   key: 'username'
  // },
  {
    title: '头像',
    dataIndex: 'coverUrl',
    key: 'coverUrl'
  },
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name'
  },
  {
    title: '性别',
    dataIndex: 'sex',
    key: 'sex'
  },
  {
    title: '出生日期',
    dataIndex: 'birthday',
    key: 'birthday'
  },
  {
    title: '学历',
    dataIndex: 'education',
    key: 'education'
  },
  {
    title: '毕业学校',
    dataIndex: 'school',
    key: 'school'
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
    classification: undefined,
    tag: [],
    imageFile: undefined,
    status: undefined,
    birthday: undefined,
    education: undefined,
    school: undefined
  },
  rules: {
    name: [{ required: true, message: '请输入', trigger: 'change' }],
    status: [{ required: true, message: '请选择状态', trigger: 'change' }]
  },
});

const myform = ref<FormInstance>();

onMounted(() => {
  getDataList();
});

const getDataList = () => {
  data.loading = true;
  listApi({
    keyword: data.keyword,
  })
      .then((res) => {
        data.loading = false;
        console.log(res);
        res.data.forEach((item: any, index: any) => {
          item.index = index + 1;
          item.coverUrl = BASE_URL +  '/api/staticfiles/resume/' + item.cover
        });
        data.dataList = res.data;
      })
      .catch((err) => {
        data.loading = false;
        console.log(err);
      });
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
        if (modal.form.classification) {
          formData.append('classification', modal.form.classification)
        }
        if (modal.form.tag) {
          modal.form.tag.forEach(function (value) {
            if(value){
              formData.append('tag', value)
            }
          })
        }

        formData.append('description', modal.form.description || '')

        if(modal.form.imageFile) {
          formData.append("coverFile", modal.form.imageFile)
        }

        if (modal.form.status) {
          formData.append('status', modal.form.status)
        }
        if (modal.form.name) {
          formData.append('name', modal.form.name)
        }
        if (modal.form.sex) {
          formData.append('sex', modal.form.sex)
        }
        if (modal.form.education) {
          formData.append('education', modal.form.education)
        } if (modal.form.birthday) {
          formData.append('birthday', modal.form.birthday)
        } if (modal.form.school) {
          formData.append('school', modal.form.school)
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
          // createApi(formData)
          //     .then((res) => {
          //       hideModal();
          //       getDataList();
          //     })
          //     .catch((err) => {
          //       console.log(err);
          //       message.error(err.msg || '操作失败');
          //     });
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
