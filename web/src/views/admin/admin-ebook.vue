<template>

  <a-layout>

    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
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
            <a-button type="primary">
              编辑
            </a-button>
            <a-button type="danger">
              删除
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue'
import axios from 'axios'

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
        }
      }).then((response) =>{
        loading.value = false;
        const data = response.data;

        //ebooks.value = data.content.list;
        //由于后端返回的resp变了，所以修改为
        ebooks.value = data.content.list;
        //重置分页按钮
        pagination.value.current = p.page;
        pagination.value.total = data.content.total;
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

    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange
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