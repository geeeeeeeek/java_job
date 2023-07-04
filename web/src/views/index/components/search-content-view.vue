<template>
  <div class="content-margin">
    <h1 class="search-name-box">{{ tData.keyword }}</h1>
    <div class="search-tab-nav clearfix">
      <div class="tab-text">
        <span>与</span>
        <span class="strong">{{ tData.keyword }}</span>
        <span>相关的内容</span>
      </div>
    </div>
    <div class="content-list">
      <div class="thing-list">

        <a-spin :spinning="tData.loading" style="min-height: 200px;">
          <div class="things flex-view">
            <div class="sub-li" v-for="item in tData.pageData" @click="handleDetail(item)">
              <a class="job-info" target="_blank">
                <div class="sub-li-top">
                  <div class="sub-li-info">
                    <p class="name">{{item.title}}</p>
                  </div>
                  <p class="salary">{{item.salary}}</p>
                </div>
                <p class="job-text">
                  <span>{{item.location}}</span>
                  <span>{{item.work_expe}}</span>
                  <span>{{item.education}}</span>
                </p>
              </a>
            </div>

          </div>
        </a-spin>
        <div class="page-view" style="">
          <a-pagination v-model:value="tData.page" size="small" @change="changePage" :hideOnSinglePage="true"
                        :defaultPageSize="tData.pageSize" :total="tData.total"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {listApi as listThingList} from '/@/api/thing'
import {BASE_URL} from "/@/store/constants";
import {useUserStore} from "/@/store";

const userStore = useUserStore()
const router = useRouter();
const route = useRoute();

const tData = reactive({
  loading: false,
  keyword: '',
  thingData: [],
  pageData: [],

  page: 1,
  total: 0,
  pageSize: 20,
})

onMounted(() => {
  search()
})

// 监听query参数
watch(() => route.query, (newPath, oldPath) => {
  search()
}, {immediate: false});


const search = () => {
  tData.keyword = route.query.keyword.trim()
  getThingList({'keyword': tData.keyword})
}

// 分页事件
const changePage = (page) => {
  tData.page = page
  let start = (tData.page - 1) * tData.pageSize
  tData.pageData = tData.thingData.slice(start, start + tData.pageSize)
  console.log('第' + tData.page + '页')
}
const handleDetail = (item) => {
  // 跳转新页面
  let text = router.resolve({name: 'detail', query: {id: item.id}})
  window.open(text.href, '_blank')
}
const getThingList = (data) => {
  tData.loading = true
  listThingList(data).then(res => {
    res.data.forEach((item, index) => {
      if (item.cover) {
        item.cover = BASE_URL + '/api/staticfiles/image/' + item.cover
      }
    })
    tData.thingData = res.data
    tData.total = tData.thingData.length
    changePage(1)
    tData.loading = false
  }).catch(err => {
    console.log(err)
    tData.loading = false
  })
}

</script>
<style scoped lang="less">
.content-margin {
  margin: 156px 0 100px;
}

.page-view {
  width: 100%;
  text-align: center;
  margin-top: 48px;
}

.search-name-box {
  background: #f5f9fb;
  height: 100px;
  line-height: 100px;
  font-size: 20px;
  color: #152844;
  text-align: center;
  position: fixed;
  top: 56px;
  left: 0;
  z-index: 1;
  width: calc(100% - 8px);
}

.search-tab-nav {
  position: relative;
  padding: 24px 0 16px;
  text-align: center;

  .tab-text {
    float: left;
    color: #5f77a6;
    font-size: 14px;
  }

  .strong {
    color: #152844;
    font-weight: 600;
    margin: 0 4px;
  }
}

.things {
  -ms-flex-wrap: wrap;
  flex-wrap: wrap;
}

.flex-view {
  display: flex;
  gap: 24px;
}

.sub-li {
  background-color: #f6fbfb;
  height: 120px;
  overflow: hidden;
  transition: all .2s linear;
  display: block;
  width: 250px;
  font-size: 0;
  padding: 16px 20px;
  box-sizing: border-box;
  .job-info {
    padding: 16px 20px;
    box-sizing: border-box;
  }
  .sub-li-top {
    margin-bottom: 12px;
    display: flex;
    width: 100%;
    align-items: center;
    .name {
      color: #222;
      font-size: 16px;
      font-weight: 500;
      line-height: 22px;
      transition: all .2s linear;
      position: relative;
      max-width: 200px;
      margin-right: 8px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .salary {
      font-size: 16px;
      font-weight: 500;
      color: #fe574a;
      line-height: 22px;
      flex: none;
    }
  }
  .sub-li-info {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    height: 22px;
    overflow: hidden;
    flex: 1;
  }
  .job-text {
    white-space: normal;
    padding-right: 0;
    height: 22px;
    line-height: 22px;
    overflow: hidden;
    word-break: break-all;
    max-width: none;
    span {
      display: inline-block;
      height: 18px;
      font-size: 13px;
      font-weight: 400;
      color: #666;
      line-height: 18px;
      padding-right: 20px;
      border-radius: 4px;
      background: #f8f8f8;
    }
  }
}

.a-price-symbol {
  top: -0.5em;
  font-size: 12px;
}

.a-price {
  color: #0F1111;
  font-size: 21px;
}

</style>
