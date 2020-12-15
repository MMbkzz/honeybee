<template>
  <div>
    <div class="side-logo" v-if="sidebar.opened">
      <router-link to="/home">
        <img src="../../../../assets/tdspic/white5.png">
      </router-link>
      <!-- <div class="logo-title"><span>数据服务平台</span></div> -->
    </div>
    <div class="little-side-logo" v-else>
      <!-- <img src="../../../../assets/tdspic/logo-zhe.png"> -->
    </div>
    <scroll-bar>
      <el-menu mode="vertical" unique-opened :default-active="$route.path" :collapse="isCollapse" background-color="transparent" text-color="#fff" active-text-color="#F99C35">
        <sidebar-item :routes="permission_routers"></sidebar-item>
      </el-menu>
    </scroll-bar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'
import ScrollBar from '@/components/ScrollBar'

// import { asyncRouterMap, constantRouterMap } from '@/router'


// window.onload = function() {
//   const elMenuItems = document.getElementsByClassName('el-menu-item')
//   const elSubMenuTitles = document.getElementsByClassName('el-submenu__title')
//   for(let i=0;i<elMenuItems.length;i++){
//     elMenuItems[i].onmouseenter = function(){
//       // console.log(this.style.backgroundColor)
//       this.style.backgroundColor = 'rgba(0, 0, 0, 0.2)'
//     }
//   }
//   for(let i=0;i<elSubMenuTitles.length;i++){
//     elSubMenuTitles[i].onmouseenter = function(){
//       this.style.backgroundColor = 'rgba(0, 0, 0, 0.2)'
//     }
//     elSubMenuTitles[i].getElementsByClassName('el-icon-arrow-down')[0].style.color = 'white'
//   }
//   $('.el-menu-item,.el-submenu__title').css({'height':'40px','line-height':'40px'})
//   $('.el-menu .router-link-active').css({'background':'rgba(0,0,0,.2)','border-left':'3px solid #4562fc'})
//   $('.el-menu a').click(function(){
//     $('.el-menu a').css({'background':'transparent','border-left':'3px solid transparent'})
//     $(this).css({'background':'rgba(0,0,0,.2)','border-left':'3px solid #4562fc'})
//   })
// }


export default {
  components: { SidebarItem, ScrollBar },
  data() {
    return {
      // permission_routers:constantRouterMap.concat(asyncRouterMap)
      // permission_routers: constantRouterMap
    }
  },
  computed: {
    ...mapGetters([
      'permission_routers',
      'sidebar'
    ]),
    isCollapse() {
      return !this.sidebar.opened
    }
  },
  mounted() {

  },
  methods: {
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .side-logo {
    position: absolute;
    z-index: 10;
    width: 230px;
    height: 140px;
    line-height: 60px;
    //background: url('../../../../assets/tdspic/treeBg.jpg') no-repeat;
    img {
      vertical-align: middle;
      margin: 40px 0 0 25px;
      width: 180px;
    }
    .logo-title{
      height: 40px;
      display: flex;
      flex-direction: column;
      justify-content: flex-end;
      span {
        color: #fff;
        font-size: 24px;
        font-family: 'SourceHanSansCN-Bold';
        margin-left: 20px;
      }
    }

  }
  .little-side-logo {
    position: absolute;
    z-index: 10;
    width: 36px;
    height: 60px;
    line-height: 60px;
    background-color: transparent;
    img {
      margin: 10px 12px;
      width: 36px;
      height: 36px;
    }
  }
</style>
<style lang="scss">
  .scroll-container{
    .el-menu{
      a{
        border-left: 3px solid transparent;
        &:nth-of-type(3){
          margin-bottom:30px;
        }
      }
      background: transparent;
      .router-link-active{
        background: rgba(0,0,0,.2);
        border-left: 3px solid #4562fc;
        .el-menu-item{
          height: 40px;
          line-height: 40px;
          background-color: rgba(0, 0, 0, 0.2);
        }
        .el-submenu__title{
          height: 40px;
          line-height: 40px;
          background-color: rgba(0, 0, 0, 0.2);
        }
      }
      .el-menu-item{
        height: 40px;
        line-height: 40px;
        background: transparent;
        &:hover{
          background-color: rgba(0, 0, 0, 0.2)!important;
        }
      }
      .el-submenu{
        a{
          margin-bottom: 0;
        }
        .el-submenu__title{
          height: 40px;
          line-height: 40px;
          border-left: 3px solid transparent;
          i{
            color: white!important;
            display: none;
          }
          &:hover{
            background-color: rgba(0, 0, 0, .2)!important;
          }
        }
      }
    }
  }
</style>
