<template>
  <div>
    <nav class="components-nav clear">
      <span class="title fl">
        <router-link to="/servicemanage">数据服务管理 > </router-link>
      <span class="server-details">服务详情</span>
      </span>
    </nav>
    <section>
      <div class="code-detail section-bod-pd">
        <template>
            <el-table
              :data="serviceTable"
              style="width: 100%"
              >
              <el-table-column prop="code" label="服务编号" width="180" header-align="center" align="center">
              </el-table-column>
              <el-table-column prop="model" label="服务模型" width="180" header-align="center" align="center">
              </el-table-column>
              <el-table-column prop="type" label="服务类型" header-align="center" align="center">
              </el-table-column>
              <el-table-column prop="status" label="服务状态" header-align="center" align="center">
              </el-table-column>
              <el-table-column prop="url" label="URL" header-align="center" align="center">
              </el-table-column>
            </el-table-column>
            </el-table>
        </template>
      </div>
      <div class="user-param clearfix section-bod-pd">
        <div class="service-users fl">
          <span class="user-span">用户：</span>
          <ul class="list">
            <li v-for="(item, index) in userDetail">
              <el-input v-model="userDetail[index].massage" style="height:30px; width: 80%;"></el-input>
              <span class="copy-span">复制</span>
            </li>
          </ul>
           <div class="text-r mt-12">
              <el-button type="success" size="mini" @click="addUser()">添加用户</el-button>
          </div>
        </div>
        <div class="param-table fr">
          <span class="user-span">属性列表：</span>
          <template>
            <el-table 
              :data="detailSttr"
              border
              :header-row-class-name="backgroundColorBorderChange"
              class="mt-15">
              <el-table-column label="属性名" header-align="center" align="center" prop="name">
              </el-table-column>
              <el-table-column label="类型" header-align="center" align="center" prop="type">
              </el-table-column>
              <el-table-column label="描述" header-align="center" align="center" prop="des">
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
      <div class="section-bod-pd mt-15">
        <span class="user-span">参数列表：</span>
        <template>
          <el-table 
            :data="detailParam"
            border
            :header-row-class-name="backgroundColorBorderChange"
            class="mt-15">
            <el-table-column label="参数名" header-align="center" align="center" prop="name">
            </el-table-column>
            <el-table-column label="描述" header-align="center" align="center" prop="des">
            </el-table-column>
            <el-table-column label="类型" header-align="center" align="center" prop="type">
            </el-table-column>
            <el-table-column label="是否必填" header-align="center" align="center" prop="manda">
            </el-table-column>
          </el-table>
        </template>
      </div>
    </section>
  </div>
</template>

<script>

export default {
  data() {
    return {
      serviceTable: [
        {
          code: '2',
          model: '3',
          type: '2',
          status: '1',
          url: '3'
        } 
      ],
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
          choose: false 
        }
      ],
      detailParam: [
        {
          name: 'P1',
          des: '',
          type: '',
          manda: 'N'
        },{
          name: 'P2',
          des: '',
          type: '',
          manda: 'N'
        },{
          name: 'P3',
          des: '',
          type: '',
          manda: 'Y'
        },{
          name: 'P4',
          des: '',
          type: '',
          manda: 'Y'
        }
      ],
      userDetail: [
        {
          massage: 'A TOKEN'
        },{
          massage: 'B TOKEN'
        },{
          massage: 'C TOKEN'
        }
      ]
    }
  },
  methods: {
    // 添加用户函数
    addUser(){
      this.userDetail.push(
        {
          massege: ''
        }
      )
    },
    // el-table的背景改变函数
    backgroundColorChange({row, rowIndex}){
      return 'table-header-bg'
    },
    backgroundColorBorderChange({row, rowIndex}){
      return 'table-header-border-bg'
    }
  }
}
</script>

<style lang='scss' scoped>
.components-nav {
  height: 75px;
  padding-top: 20px;
  background: #f1f1f1;
  .server-details {
    color: #3a8fe0;
  }
}
.code-detail {
  border: 1px solid #e8e8e8;
}
.user-param {
  border: 1px solid #e8e8e8;
  padding: 20px 30px;
  margin-top: 15px;
  .service-users {
    width: 45%;
    .list{
      height: 206px;
      overflow: auto;
    }
  }
  .param-table {
    width: 45%;
  }
}
ul li{
  list-style: none;
  font-size: 14px;
  color: #a1a6a7;
  height: 30px;
  margin-top: 20px;
}
.copy-span {
  padding: 2px 6px;
  border: 1px solid #ddd;
  border-radius: 4px;
  float: right;
}
.section-bod-pd {
  padding: 20px 30px;
  border: 1px solid #e9e9e9; 
  background: #fff; 
  box-shadow: 0px 2px 2px #ddd;
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