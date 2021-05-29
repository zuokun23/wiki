<template>

  <a-layout>

    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form
            layout="inline"
            :model="param"

        >
          <a-form-item>
            <a-input v-model:value="param.name" placeholder="名称">
              <template #prefix><UserOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({
            page: 1,
            size: pagination.pageSize,
            name: param.name
            })" size="large">
              查询
            </a-button>
          </a-form-item>
          <a-button type="primary" @click="add()" size="large">
            新增
          </a-button>
        </a-form>

      </p>
      <a-table :columns="columns"
               :data-source="ebooks"
               :pagination="pagination"
               :loading="loading"
               :row-key="record => record.id"
               @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" />
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
<!--            设置组件之间的间距-->
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>

            <a-popconfirm
                title="删除后不可恢复，确认删除？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
                @cancel="cancel"
            >
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>

          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

<!--  modal对话框-->
  <a-modal
      title="电子书表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类一">
        <a-input v-model:value="ebook.category1Id" />
      </a-form-item>
      <a-form-item label="分类二">
        <a-input v-model:value="ebook.category2Id" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="textarea"/>
      </a-form-item>

    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue'
import axios from 'axios'
import { message } from 'ant-design-vue'
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminEbook',
  setup(){
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      //修改为4页
      pageSize: 4,
      total: 0
    });

    const loading = ref(false);

    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: { customRender: 'cover'}
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类一',
        key: 'category1Id',
        dataIndex: 'category1Id'
      },
      {
        title: '分类二',
        dataIndex: 'category2Id'
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action'}
      }
    ];
    /**
     * 数据查询
     */
    const handleQuery = (p: any) => {
      //loading是一个等待框的效果
      loading.value = true;
      axios.get("/ebook/list", {
        params: {
          page: p.page,
          size: p.size,
          name: p.name
        }
      }).then((response) =>{
        loading.value = false;
        const data = response.data;

        if(data.success){
          //ebooks.value = data.content.list;
          //由于后端返回的resp变了，所以修改为
          ebooks.value = data.content.list;
          //重置分页按钮
          pagination.value.current = p.page;
          pagination.value.total = data.content.total;
        }else{
          message.error(data.message);
        }

      })
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        //这里pagination是局部变量
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    onMounted(() => {
      handleQuery({
        page: pagination.value.current,
        //响应式变量：页面大小
        size: pagination.value.pageSize
      });
    });

    /**
     * modal对话框
     */
    const ebook = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;

      axios.post("/ebook/save", ebook.value).then((response) =>{
        const data = response.data;//data == commonResp
        modalLoading.value = false;
        if(data.success){
          modalVisible.value = false;
          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }else{
          message.error(data.message);
        }
      })
    };
    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      ebook.value = Tool.copy(record);
    }
    /**
     * 新增
     */
    const add = (record: any) => {
      modalVisible.value = true;
      ebook.value = {};
    }
    /**
     * 删除
     * @param id
     */
    const handleDelete = (id: number) => {
      axios.delete("/ebook/delete/" + id).then((response) => {
        const data = response.data; // data = commonResp
        if(data.success){

          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          })
        }
      })
    }

    /**
     * 根据name查询列表
     */
    const param = ref();
    param.value = {};


    return {
      //表格
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,

      //新增与修改,与删除
      edit,
      add,
      handleDelete,
      //根据name查询列表
      param,
      handleQuery,

      //表单
      ebook,
      handleModalOk,
      modalVisible,
      modalLoading
    }
  }

})

</script>

<style scoped>
  img {
    width: 50px;
    height: 50px;
  }
</style>