<template>
  <div>
    <nav class="components-nav clear">
      <div>
        <span class="title">
          <router-link to="/servicemanage">数据服务管理 > </router-link>
          <span class="server-details">验证</span>
        </span>
      </div>
      <div class="pt-25 pb-15">
        <span class="user-span">URL:</span>
        <span style="height: 20px; border-bottom: 1px solid #ddd;color: #a1a6a7;">https://segmentfault.com/a/1190000004149236</span>
      </div>
    </nav>
    <section class="component-section">
      <!-- 用户-属性列表 -->
      <div class="user-param clearfix section-bod">
        <div class="service-users fl">
          <!--验证用户选择-->
          <div class="user-span">验证用户：</div>
          <template>
            <el-select v-model="validaUser" placeholder="请选择" class="mt-15">
              <el-option
                v-for="item in userDetail"
                :key="item.massage"
                :label="item.massage"
                :value="item.massage"
                :disabled="item.disabled">
              </el-option>
            </el-select>
          </template>
          <!--<ul class="list">
            <li v-for="(item, index) in userDetail">{{ item.massage }}<span class="copy-span">复制</span></li>
          </ul>-->
          <!--属性列表-->
          <div class="mt-15">
            <div class="user-span">属性列表：</div>
            <template>
              <el-table 
                :data="detailSttr"
                border
                :header-row-class-name="backgroundColorBorderChange"
                class="mt-15">
                <el-table-column label="属性名" header-align="center" align="center" prop="name" width="150">
                </el-table-column>
                <el-table-column label="类型" header-align="center" align="center" prop="type" width="150">
                </el-table-column>
                <el-table-column label="描述" header-align="center" align="center" prop="des" width="150">
                </el-table-column>
                <el-table-column label="表达式" header-align="center" align="center" prop="express">
                </el-table-column>
                <el-table-column label="是否选择" header-align="center" align="center">
                  <template slot-scope="scope">
                    <el-checkbox v-model="scope.row.choose"></el-checkbox>
                  </template>
                </el-table-column>
              </el-table>
            </template>
          </div>
        </div>
        <!-- 参数列表 -->
        <div class="param-table fr">
          <span class="user-span">参数列表：</span>
          <template>
            <el-table 
              :data="detailParam"
              border
              :header-row-class-name="backgroundColorBorderChange"
              class="mt-15">
              <el-table-column
                label="参数名" 
                header-align="center" 
                align="center">
                <template slot-scope="scope">
                  <span :class="{'must-before': scope.row.manda}">{{ scope.row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="参数值" 
                header-align="center" 
                align="center">
                <template slot-scope="scope">
                  <el-input v-model="name" placeholder=""></el-input>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </div>
      </div>
      <!-- json -->
      <div class="clearfix mt-20 section-bod" style="padding: 20px 30px">
        <div class="fl wd45">
          <span class="user-span">参数json</span>
          <el-input class="mt-12" type="textarea" :autosize="{ minRows: 8, maxRows: 12}" v-model="paramJson"></el-input>
        </div>
        <div class="fr wd45">
          <span class="user-span">结果json</span>
          <el-input class="mt-12" type="textarea" :autosize="{ minRows: 8, maxRows: 12}" v-model="resultJson"></el-input>
        </div>
      </div>
      <div class="text-center mt-20">
        <el-button type="primary">测试</el-button>
        <el-button>样例下载</el-button>
      </div>
    </section>

  </div>
</template>

<script>

export default {
  props: ['id'],
  data() {
    return {
      // 选择用户
      userDetail: [
        {
          massage: 'A TOKEN'
        },{
          massage: 'B TOKEN'
        },{
          massage: 'C TOKEN'
        }
      ],
      // 选择属性
      detailSttr: [
        {
          name: 'mem_name',
          type: '样式',
          des: '123',
          express: '123',
          choose: true 
        },{
          name: 'mem_name',
          type: '样式',
          des: '123',
          express: '123',
          choose: true 
        }
      ],
      // 参数列表
      detailParam: [
        {
          name: 'P1',
          manda: true
        },{
          name: 'P2',
          manda: true
        },{
          name: 'P3',
          manda: false
        },{
          name: 'P4',
          manda: false
        }
      ],
      validaUser: '', // 验证用户
      urlInput: '', // URL
      paramJson: '', // 参数json
      resultJson: '' // 结果json
    }
  },
  methods: {
    // el-table的背景改变函数
    backgroundColorChange({row, rowIndex}){
      return 'table-header-bg'
    },
    backgroundColorBorderChange({row, rowIndex}){
      return 'table-header-border-bg'
    }
  },
  mounted() {
    console.log(this.id)
  }
}
</script>

<style lang="scss" scoped>
.components-nav {
  padding-top: 20px;
  background: #f1f1f1;
  .server-details {
    color: #3a8fe0;
  }
  .titile {
    color: #444;
    font-size: 23px;
  }
}
.component-section {
  .user-param {
    border: 1px solid #e8e8e8;
    padding: 20px 30px;
    .service-users {
      width: 60%;
      .list{
        height: 206px;
        overflow: auto;
      }
    }
    .param-table {
      width: 36%;
    }
  }
  ul li{
    list-style: none;
    font-size: 14px;
    color: #a1a6a7;
    height: 30px;
    margin-top: 20px;
    border-bottom: 1px solid #efefef;
  }
}
.section-bod {
  border: 1px solid #e9e9e9; 
  background: #fff; 
  box-shadow: 0px 2px 2px #ddd;
}
.must-before {
    position: relative; 
}
.must-before:before {
    content: "*";
    color: #F56461;
    position: absolute;
    left: -10px;
    top: -2px; 
}
</style>
<style>
  .table-header-bg>th{
    background-color: #f0f0f0 !important;
    border: 1px solid #f0f0f0;
  }
  .table-header-border-bg>th{
    background-color: #f0f0f0 !important;
    border: 1px solid #e7e7e7;
  }
</style>