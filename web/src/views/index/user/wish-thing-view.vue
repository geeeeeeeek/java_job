<template>
  <div class="content-list">
    <div class="list-title">我的心愿单</div>
    <div role="tablist" class="list-tabs-view flex-view">
    </div>
    <div class="list-content">
      <div class="collect-thing-view">
        <div class="thing-list flex-view">
          <div class="sub-li" v-for="item in wishData" @click="handleClickItem(item)">
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
      </div>
    </div>
  </div>
</template>

<script setup>
import {message} from 'ant-design-vue';
import {userWishListApi, unWishApi} from '/@/api/thingWish'
import {BASE_URL} from "/@/store/constants";
import {useUserStore} from "/@/store";

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

let wishData = ref([])

onMounted(()=>{
  getWishThingList()
})

const handleClickItem =(record)=> {
  let text = router.resolve({name: 'detail', query: {id: record.id}})
  window.open(text.href, '_blank')
}

const handleRemove =(record)=> {
  unWishApi({id:record.id}).then(res => {
    message.success('移除成功')
    getWishThingList()
  }).catch(err => {
    console.log(err)
  })
}
const getWishThingList =()=> {
  let userId = userStore.user_id
  userWishListApi({userId: userId}).then(res => {
    res.data.forEach(item => {
      item.cover = BASE_URL + '/api/staticfiles/image/' + item.cover
    })
    wishData.value = res.data
  }).catch(err => {
    console.log(err.msg)
  })
}

</script>
<style scoped lang="less">
.flex-view {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
}

.content-list {
  -webkit-box-flex: 1;
  -ms-flex: 1;
  flex: 1;

  .list-title {
    color: #152844;
    font-weight: 600;
    font-size: 18px;
    line-height: 24px;
    height: 24px;
    margin-bottom: 4px;
  }

  .list-tabs-view {
    position: relative;
    border-bottom: 1px solid #cedce4;
    height: 12px;
    line-height: 42px;
  }
}

.thing-list {
  -ms-flex-wrap: wrap;
  flex-wrap: wrap;
  -webkit-box-pack: start;
  -ms-flex-pack: start;
  justify-content: flex-start;
  padding-top: 24px;
  gap: 24px;

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

}
</style>
