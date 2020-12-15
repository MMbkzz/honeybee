<!--数据源监控界面-->
<template>
  <div class="source-moni">
    <div class="secondMenu">
      <div v-for="(item,index) in list">
        <div class="menu-title" @click="show(index)" :class="{ 'menu-title-active': showIndex == index&&secondIndex == '-1' }">
          <span :class="{'triangle-up': showIndex == index, 'triangle-down': showIndex != index}"></span>
          {{ item.title }}
        </div>
        <!--<div class="menu-content" :class="{active: showIndex == index}">
          <div v-for="(c, i) in item.content">{{ c }}</div>
        </div>-->
        <div class="menu-content" :class="{active: showIndex == index}">
          <transition-group name="fade">
            <div v-for="(c, i) in item.content" v-if="showIndex == index" :key="c" @click="secondActive(i)" :class="{'div-active': secondIndex == i}">
              {{ c }}
            </div>
          </transition-group>
        </div>
      </div>
    </div>
    <div class="source-content">
      <nav class="components-nav clear">
        <span class="title fl">数据源监控</span>
        <div class="fr">
          <span class="add">
            <svg-icon icon-class="add" style="width: 18px; height: 18px"></svg-icon>
            <span>新增</span>
          </span>
          <span class="add">
            <span><svg-icon icon-class="deletelall" style="width: 18px; height: 18px"></svg-icon></span>
            <span>删除</span>
          </span>
        </div>
        <div class="search fr">
          <input type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <svg-icon icon-class="search"></svg-icon>
        </div>
      </nav>
      <section class="components-container clearfix section-bod" v-if="menuTable == '1'" key="sourceTable">
        <el-table ref="multipleTable"
          :data="sourceTable"
          tooltip-effect="light"
          style="width: 100%"
          height = '400'>
          <el-table-column prop="code" label="实例编号" show-overflow-tooltip width="100">
          </el-table-column>
          <el-table-column prop="name" label="实例名称" show-overflow-tooltip width="100">
          </el-table-column>
          <el-table-column prop="ip" label="IP" show-overflow-tooltip width="100">
          </el-table-column>
          <el-table-column prop="port" show-overflow-tooltip label="端口号" width="100">
          </el-table-column>
          <el-table-column label="预期持有连接数">
            <template slot-scope="scope">
              <div class="progressContainer">
                  <div class="progress-gary" :style="{width:scope.row.expectedNum+'%'}">
                    <b>{{scope.row.expectedNum}}</b>
                  </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="已持有连接数">
            <template slot-scope="scope">
              <div class="progressContainer">
                <div class="progress-orange" :style="{width:scope.row.ownNum/scope.row.expectedNum*100+'%'}">
                  <b>{{scope.row.ownNum}}</b>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="已使用有连接数">
            <template slot-scope="scope">
              <div class="progressContainer">
                <div class="progress-green" :style="{width:scope.row.used/scope.row.expectedNum*100+'%'}">
                  <b>{{scope.row.used}}</b>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="剩余连接数">
            <template slot-scope="scope">
              <div class="progressContainer">
                <div class="progress-blue" :style="{width:scope.row.remain/scope.row.expectedNum*100+'%'}">
                  <b>{{scope.row.remain}}</b>
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          background
          layout="prev, pager, next"
          :total="100"
          class="fr mt-15">
        </el-pagination>
      </section>
      <section class="components-container clearfix section-bod" v-else key="instanceTable">
        <el-table ref="multipleTable"
          :data="instanceTable"
          tooltip-effect="light"
          style="width: 100%"
          height = '400'
          >
          <el-table-column prop="drive" label="数据驱动" show-overflow-tooltip >
          </el-table-column>
          <el-table-column prop="state" label="状态" show-overflow-tooltip >
          </el-table-column>
          <el-table-column prop="maxLink" label="最大连接数" show-overflow-tooltip >
          </el-table-column>
          <el-table-column prop="connected" label="已连接数" show-overflow-tooltip >
          </el-table-column>
          <el-table-column prop="remainConnect" label="剩余连接数" show-overflow-tooltip >
          </el-table-column>
        </el-table>
        <el-pagination
          background
          layout="prev, pager, next"
          :total="100"
          class="fr mt-15">
        </el-pagination>
      </section>
    </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      query: {
        name: ''
      },
      // 数据源数据
      sourceTable: [
        {
          code:  'ERP财务数据库',
          name: '类型',
          ip: '192.168.1.1',
          port: '201',
          expectedNum: '20',
          ownNum: '15',
          used: '9',
          remain: '6'
        },{
          code:  'ERP财务数据库',
          name: '类型',
          ip: '192.168.1.1',
          port: '201',
          expectedNum: '20',
          ownNum: '15',
          used: '9',
          remain: '6'
        }
      ],
      // 实例数据
      instanceTable: [
        {
          drive: 'XXX',
          state: '已发布',
          maxLink: '12',
          connected: '2',
          remainConnect: '5'
        },{
          drive: 'XXX',
          state: '发布',
          maxLink: '12',
          connected: '2',
          remainConnect: '5'
        }
      ],
      // 二级菜单
      list: [
        {
          title: '数据源A',
          content: ['实例1','实例2','实例3']
        },{
          title: '数据源B',
          content: ['实例A','实例B']
        },{
          title: '数据源C',
          content: ['实例A','实例B']
        },{
          title: '数据源D',
          content: ['实例A','实例B']
        }
      ],
      showIndex: '1',
      secondIndex: '-1',
      menuTable: '1',
      progress: 50, // 进度条
    }
  },
  methods: {
     // 一级菜单点击
    show(i) {
      this.showIndex = i;
      this.secondIndex = '-1';
      this.menuTable = '1';
    },
      // 二级菜单点击
    secondActive(i){
      // this.showIndex = '-1';
      this.secondIndex = i;
      this.menuTable = '2';
    }
  }
}
</script>

<style lang="scss" scoped>
.source-moni {
  height: 100%;
  margin-top: -15px;
  margin-left: -15px;
  .secondMenu {
    position: absolute;
    top: 0;
    bottom: 0;
    height: 100%;
    width: 180px;
    background: #e1e4e9;
    padding-top: 25px;
    .menu-title {
      height: 40px;
      line-height: 40px;
      color: rgba(50,63,79,1);
      font-size: 14px;
      padding-left: 20px;
      cursor: pointer;
    }
    .menu-title:hover{
      background: rgba(241, 241, 241, 1)
    }
    .menu-title-active{
      background: rgba(241, 241, 241, 1)
    }
    .menu-content {
      padding-bottom: 20px;
      display: none;
      transition: display 2s;
      div{
        cursor: pointer;
        height: 35px;
        line-height: 35px;
        padding-left: 60px;
        color: rgba(46,63,79,1);
        font-size: 14px;
      }
      div:hover{
        background: rgba(241, 241, 241, 1)
      }
      .div-active{
        background: rgba(241, 241, 241, 1)
      }
    }
    .active {
      display: block;
    }
  }
  .source-content {
    padding-left: 30px;
    margin-left: 180px;
    .components-nav {
      height: 75px;
      padding-top: 20px;
      background: #f1f1f1;
      .titile {
        color: #444;
        font-size: 23px;
      }
    }
  }
}
.triangle-up {
  display: inline-block;
  width: 0;
  height: 0;
  border-top: 5px solid rgba(50,63,79,1);
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  margin-bottom: 2px;
  margin-right: 10px;
}
.triangle-down {
  display: inline-block;
  width: 0;
  height: 0;
  border-bottom: 5px solid rgba(50,63,79,1);
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  margin-bottom: 2px;
  margin-right: 10px;
}
.fade-enter-active, .fade-leave-active {
  transition: opacity 1.5s;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}
.progressContainer{
  height: 20px;
  width: 70px;
  border-radius: 5px;
  background-color: rgba(203,207,216,1);
  margin-left: 2%;
}
.progress-orange {
  background-color: rgba(255,149,1,1);
  border-radius: 5px;
  height:20px;
  line-height: 20px;
}
.progress-gary {
  background-color: rgba(203,207,216,1);
  border-radius: 5px;
  height:20px;
  line-height: 20px;
}
.progress-green {
  background-color: rgba(118,200,14,1);
  border-radius: 5px;
  height:20px;
  line-height: 20px;
}
.progress-blue {
  background-color: rgba(65, 138, 215,1);
  border-radius: 5px;
  height:20px;
  line-height: 20px;
}
b{
  color:#fff;
  font-weight: 100;
  font-size: 12px;
  position:absolute;
  left: 15%;
 }
</style>