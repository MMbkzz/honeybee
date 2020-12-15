<template>
  <div class="menu-wrapper">
    <template v-for="item in routes" v-if="!item.hidden&&item.children">
      <!--如果该菜单存在只有一个子节点， 且子节点再没有子节点了 或子节点的子节点hidden-->
      <router-link v-if="item.children.length===1 && !item.children[0].children" :to="item.path+'/'+item.children[0].path" :key="item.children[0].name">
        <el-menu-item :index="item.path+'/'+item.children[0].path" :class="{'submenu-title-noDropdown':!isNest}">
          <div class="svg-icon-wrap">
            <svg-icon v-if="item.children[0].meta&&item.children[0].meta.icon" :icon-class="item.children[0].meta.icon" :style="{'font-size':fontSize(item.children[0].meta.icon)}"></svg-icon>
          </div>
          <span v-if="item.children[0].meta&&item.children[0].meta.title&&sidebar.opened">{{generateTitle(item.children[0].meta.title)}}</span>
          <span v-else class="menu-tooltip">{{generateTitle(item.children[0].meta.title)}}<svg-icon icon-class="menuTooltipLeft"></svg-icon></span>
        </el-menu-item>
      </router-link>
      <!-- 其他 存在子菜单的子菜单的长度不为1-->
      <el-submenu v-else :index="item.name||item.path" :key="item.name">
        <template slot="title">
          <div class="svg-icon-wrap">
            <svg-icon v-if="item.meta&&item.meta.icon" :icon-class="item.meta.icon"></svg-icon>
          </div>
          <span v-if="item.meta&&item.meta.title">{{generateTitle(item.meta.title)}}</span>
          <span class="triangle"><img src="../../../../assets/tdspic/triangle-right.png" alt=""></span>
        </template>
        <!--递归组件-->
        <template v-for="child in item.children" v-if="!child.hidden">
          <!--子节点存在，且子节点长度大于0-->
          <sidebar-item :is-nest="true" class="nest-menu" v-if="child.children&&child.children.length>0" :routes="[child]" :key="child.path"></sidebar-item>
          <!--不存在子节点-->
          <router-link v-else :to="item.path+'/'+child.path" :key="child.name">
            <el-menu-item :index="item.path+'/'+child.path">
              <div class="svg-icon-wrap">
                <span class="sub-menu-line"></span>
                <svg-icon v-if="child.meta&&child.meta.icon" :icon-class="child.meta.icon" style="font-size:15px;"></svg-icon>
              </div>
              <span v-if="child.meta&&child.meta.title">{{generateTitle(child.meta.title)}}</span>
            </el-menu-item>
          </router-link>
        </template>
      </el-submenu>

    </template>
  </div>
</template>

<script>
import { generateTitle } from '@/utils/i18n'
import { mapGetters } from 'vuex'
export default {
  name: 'SidebarItem',
  props: {
    routes: {
      type: Array
    },
    isNest: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    ...mapGetters([
      'sidebar'
    ])
  },
  methods: {
    generateTitle,
    get() {
      console.log('导航菜单',this.routes)
    },
    fontSize(val){
      // console.log(val)
      let fz = '';
      if(val === 'asset'){
        fz = '20px'
      }else if(val === 'service02'){
        fz = '22px'
      }else{
        fz = '18px'
      }
      return fz
    }
  },
  mounted() {
    this.get();
  }
}
</script>
<style lang="scss" scoped>
.triangle{
  display: flex;
  float: right;
  height: 100%;
  flex-direction: column;
  justify-content: center;
  img{
    width: 13px;
    transition: transform 0.3s;
  }
}
.el-submenu{
  &.is-opened{
    .triangle{
      display: inline-block;
      img{
        transform: rotate(90deg)
      }
    }
  }
}
</style>
