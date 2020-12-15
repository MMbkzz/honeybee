<!-- 系统参数配置页面 -->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">系统参数配置 </h2>
        </div>
      </div>
    </nav>
    <section class="components-container section-bod">
      <!-- <el-tabs type="border-card">
        <el-tab-pane label="meta db">
          <el-collapse>
            <el-collapse-item title="基本参数">
              <el-form ref="metaForm" :model="metaForm" label-width="100px">
                <el-form-item label="主机IP:">
                  <el-input v-model="metaForm.name"></el-input>
                </el-form-item>
                <el-form-item label="端口:">
                  <el-input v-model="metaForm.name"></el-input>
                </el-form-item>
                <el-form-item label="用户名:">
                  <el-input v-model="metaForm.name"></el-input>
                </el-form-item>
                <el-form-item label="密码:">
                  <el-input v-model="metaForm.name"></el-input>
                </el-form-item>
                <el-form-item label="默认数据库:">
                  <el-input v-model="metaForm.name"></el-input>
                </el-form-item>
              </el-form>
            </el-collapse-item>
          </el-collapse>
        </el-tab-pane>
        <el-tab-pane label="cache">
          <el-collapse>
            <el-collapse-item title="基本参数">
              <el-form ref="cacheForm" :model="cacheForm" label-width="100px">
                <el-form-item label="主机IP:">
                  <el-input v-model="cacheForm.name"></el-input>
                </el-form-item>
                <el-form-item label="端口:">
                  <el-input v-model="cacheForm.name"></el-input>
                </el-form-item>
                <el-form-item label="用户名:">
                  <el-input v-model="cacheForm.name"></el-input>
                </el-form-item>
                <el-form-item label="验证密码:">
                  <el-input v-model="cacheForm.name"></el-input>
                </el-form-item>
                <el-form-item label="默认DB:">
                  <el-input v-model="cacheForm.name"></el-input>
                </el-form-item>
              </el-form>
            </el-collapse-item>
          </el-collapse>
        </el-tab-pane>
          <el-collapse>
            <el-collapse-item :title="itemTwo.name" v-for="(itemTwo, indexTwo) in itemOne.title" :key="itemTwo.name" class="mt-15">
              <el-form ref="form" label-width="100px" v-model="itemTwo[indexTwo]">
                <el-form-item v-for="(itemThree, indexThree) in itemTwo.param" :key='indexThree' :label="itemThree.displayName" class="wd80">
                  <el-input size="mini"></el-input>
                  <span style="cursor: pointer" @click="handleUpdate"><svg-icon icon-class="editor" style="width: 18px; height: 18px"></svg-icon></span>
                  <span style="cursor: pointer" ><svg-icon icon-class="delete" style="width: 18px; height: 18px"></svg-icon></span>
                </el-form-item>
              </el-form>
              <el-button size="mini" type="primary" style="margin: 6px 0 0 100px" @click="handleCreate">添加参数</el-button>
            </el-collapse-item>
          </el-collapse>

        </el-tab-pane>
      </el-tabs> -->
      <div id="tab-box">
        <div class="side-tab">
          <ul>
            <li class="active">元数据</li>
            <li>缓存</li>
            <li v-for="(itemOne, indexOne) in  systemList" :key="indexOne">{{ itemOne.displayName }}</li>
          </ul>
        </div>
        <div class="tab-pane-wrap">
          <div class="tab-pane show">
            <el-form ref="metaForm" :model="metaForm" label-width="120px">
              <el-form-item label="IP:">
                <el-input v-model="metaForm.ip" :disabled="true"></el-input>
              </el-form-item>
              <el-form-item label="端口:">
                <el-input v-model="metaForm.port" :disabled="true"></el-input>
              </el-form-item>
              <el-form-item label="用户名:">
                <el-input v-model="metaForm.userName" :disabled="true"></el-input>
              </el-form-item>
              <el-form-item label="密码:">
                <el-input type="password" v-model="metaForm.password" :disabled="true"></el-input>
              </el-form-item>
              <el-form-item label="数据库:">
                <el-input v-model="metaForm.database" :disabled="true"></el-input>
              </el-form-item>
            </el-form>
          </div>
          <div class="tab-pane">
            <el-form ref="cacheForm" :model="cacheForm" label-width="120px">
              <el-form-item label="IP:">
                <el-input v-model="cacheForm.ip" :disabled="true"></el-input>
              </el-form-item>
              <el-form-item label="端口:">
                <el-input v-model="cacheForm.port" :disabled="true"></el-input>
              </el-form-item>
              <el-form-item label="用户名:">
                <el-input v-model="cacheForm.userName" :disabled="true"></el-input>
              </el-form-item>
              <el-form-item label="密码:">
                <el-input type="password" v-model="cacheForm.password" :disabled="true"></el-input>
              </el-form-item>
              <el-form-item label="数据库:">
                <el-input v-model="cacheForm.database" :disabled="true"></el-input>
              </el-form-item>
            </el-form>
          </div>
          <div class="tab-pane" v-for="(itemOne, indexOne) in  systemList" :key="indexOne">
            <el-collapse v-model="activeNames">
              <el-collapse-item v-for="(itemTwo, indexTwo) in itemOne.childList" :key="itemTwo.displayName" :name="indexTwo+''">
                <template slot="title">
                  {{itemTwo.displayName}}
                  <svg-icon v-if="shrink(indexTwo)" icon-class="zhankai"></svg-icon>
                  <svg-icon v-else icon-class="shouqi"></svg-icon>
                </template>
                <el-form ref="form" label-width="112px" v-model="itemTwo[indexTwo]">
                  <el-form-item v-for="(itemThree, indexThree) in itemTwo.configList" :key='indexThree' :label="itemThree.displayName">
                    <el-input v-model="itemThree.configValue"></el-input>
                    <span style="cursor: pointer" @click="handleUpdate(itemThree)"><img src="../../assets/tdspic/edit.png" style="height:18px;width:18px;position:relative;top:4px;" alt="修改"></span>
                    <span style="cursor: pointer" @click="handDelete(itemThree.id)"><img src="../../assets/tdspic/delete.png" style="height:18px;width:18px;position:relative;top:4px;" alt="删除"></span>
                  </el-form-item>
                </el-form>
                <el-button :disabled="!operatePermission" class="add-btn" type="primary" style="margin: 6px 0 0 92px" @click="handleCreate(itemOne, itemTwo)"><i>+</i>添加参数</el-button>
              </el-collapse-item>
            </el-collapse>
          </div>
        </div>
      </div>
    </section>
    <!--新增弹框-->
    <!-- <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="addDialog"
      width="50%">
      <el-form :model="form" :rules="rules" ref="ruleForm" label-width="110px" style="padding: 10px 30px;">
        <el-form-item label="参数类型：">
          <el-select v-model="form.type">
            <el-option label="类型一" value="shanghai"></el-option>
            <el-option label="类型二" value="beijing"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="参数子类型：" prop="code"  class="mt-10" required>
          <el-select v-model="form.secondType">
            <el-option label="类型一" value="shanghai"></el-option>
            <el-option label="类型二" value="beijing"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="参数名称：" class="mt-10">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="参数值：" class="mt-10">
          <el-input v-model="form.value"></el-input>
        </el-form-item>
        <el-form-item label="只读:" class="mt-10">
          <el-checkbox v-model="form.statusCode" true-label="y" false-label="n" ></el-checkbox>
        </el-form-item>
        <el-form-item label="显示名称：" class="mt-10">
          <el-input v-model="form.displayName"></el-input>
        </el-form-item>
        <el-form-item label="描述:" class="mt-10">
          <el-input type="textarea" v-model="form.des"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="creatSystem(form)">保存</el-button>
        <el-button size="mini" @click="addDialog = false">取消</el-button>
      </span>
    </el-dialog> -->
    <!-- 滑框 -->
    <slide-box
      slideWidth="500px"
      :slideShow="addDialog"
      @close="slideClose"
      :title="textMap[dialogStatus]"
      subTitle="测试"
      ref="slideBox">

      <!-- 一级弹框 -->
      <div slot="slide-content">
        <el-form :model="systemForm" :rules="systemRules" ref="systemForm" label-width="110px" style="padding: 10px 30px;">
          <el-form-item label="参数类型：" prop="configType">
            <el-select v-model="systemForm.configType" @change="fristTypeChange" size="medium">
              <el-option :label="item.displayName" :value="item.code" v-for="(item, index) in fristType" :key="index"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="参数子类型：" prop="configSubtype" class="mt-15">
            <el-select v-model="systemForm.configSubtype" size="medium">
              <el-option :label="item.displayName" :value="item.code" v-for="(item, index) in subType" :key="index"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="参数名称：" prop="configName" class="mt-15">
            <el-input v-model="systemForm.configName" size="medium"></el-input>
          </el-form-item>
          <el-form-item label="参数值：" prop="configValue" class="mt-15">
            <el-input v-model="systemForm.configValue"></el-input>
          </el-form-item>
          <el-form-item label="只读:" prop="readOnly" class="mt-15">
            <el-checkbox v-model="systemForm.readOnly" true-label="y" false-label="n" ></el-checkbox>
          </el-form-item>
          <el-form-item label="显示名称：" prop="displayName" class="mt-15">
            <el-input v-model="systemForm.displayName"></el-input>
          </el-form-item>
          <el-form-item label="描述:" prop="configDesc" class="mt-15">
            <el-input type="textarea" v-model="systemForm.configDesc"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="slide-footer">
        <el-button type="primary" size="mini" v-if="dialogStatus === 'create'" @click="creatSystem(systemForm)">保存</el-button>
        <el-button type="primary" size="mini" v-else @click="updateSystem(systemForm)">保存</el-button>
        <el-button size="mini" @click="addDialog = false">取消</el-button>
      </span>
    </slide-box>

  </div>
</template>

<script>
import request from '@/utils/request'
import SlideBox from '@/components/SlideBox'

export default {
  components: {
    SlideBox
  },
  data() {
    return {
      operatePermission: false,
      activeNames: ['0'], // 折叠面板默认展开
      systemList: [
        {
          displayName: 'Redis01',
          childList: [
            {
              name: '基本参数',
              param: [
                {
                  displayName: '数据库',
                  paramValue: 'sts'
                },{
                  displayName: '参数',
                  paramValue: 'sss'
                }
              ],
            },{
              name: '配置参数',
              param: [
                {
                  displayName: 'xxx',
                  paramValue: '123'
                },{
                  displayName: 'xxx',
                  paramValue: '333'
                }
              ],
            }
          ]
        },{
          displayName: 'Redis02',
          childList: [
            {
              name: '局部',
              param: [
                {
                  displayName: '数据库',
                  paramValue: 'sts'
                },{
                  displayName: '参数',
                  paramValue: 'sss'
                }
              ],
            },{
              name: '公共',
              param: [
                {
                  displayName: 'xxx',
                  paramValue: '123'
                },{
                  displayName: 'xxx',
                  paramValue: '333'
                }
              ],
            }
          ]
        }
      ],
      /* meta db数据 */
      metaForm: {},
      /* cache 数据 */
      cacheForm: {},
      /* 新增 */
      textMap: {
        update: '编辑',
        create: '新增'
      },
      dialogStatus: '',
      addDialog: false,
      /****** 列表  *******/
      systemForm: {
        configType: '', // 参数类型
        configSubtype: '',  // 参数子类型
        configValue: '', // 参数值
        configName: '', // 参数名
        displayName: '',  // 参数显示值
        readOnly: 'n',  // 是否只读
        configDesc: '' // 描述
      },
      /******* 新增、编辑、删除 *******/
      // 参数大类
      fristType: [],
      // 参数子类
      subType: [],
      // 是否必填或填写规则
      systemRules: {
        configType: [{ required: true, message: '参数类型' }],
        configSubtype: [{ required: true, message: '参数子类型' }],
        configName: [{ required: true, message: '参数名称' }],
        configValue: [{ required: true, message: '参数值' }],
        displayName: [{ required: true, message: '显示名称' }]
      },
    }
  },
  mounted() {
    const self = this
    if(this.$store.getters.userInfo.roles === 'administrator'){
      this.operatePermission = true
    }
    $('#tab-box li').click(function(){
      // console.log($(this).index())
      $(this).addClass('active').siblings().removeClass('active')
      $('#tab-box .tab-pane:eq('+$(this).index()+')').addClass('show').siblings().removeClass('show')
      self.activeNames = ['0']
    })
  },
  methods: {
    shrink(val) {
      return this.activeNames.find(item => item === val+'') === undefined
    },
    // 关闭滑框
    closeSlide(){
      this.$refs.slideBox.close()
      this.cancelSource()
    },
    slideClose(){
      this.addDialog = false
    },
    /********* 获取系统参数table、大类 start *********/
    fetchSystemComfig(){
      request({
        url: '/api/platform/config/query_config',
        method: 'get',
      }).then(({ data }) => {
        this.systemList = data.data
      })
    },
    // 获取meta db与cache
    fetchFixedParam(){
      request({
        url: '/api/platform/config/query',
        method: 'get',
      }).then(({ data }) => {
        this.metaForm = data.data.db
        this.cacheForm = data.data.cache
      })
    },
    // 大类获取
    fetchconfigType(){
      request({
        url: '/api/platform/config/query_config',
        method: 'get',
        params: {
          type: 'config_type'
        }
      }).then(({ data }) => {
        this.fristType = data.data
      })
    },
    // 子类获取
    fetchConfigSubType(code){
      request({
        url: '/api/service/model/query_code',
        method: 'get',
        params: {
          parentCode: code
        }
      }).then(({ data }) => {
        this.subType = data.data
      })
    },
    /********* 获取系统参数table、大类  end *********/
    /********* 新增、编辑、删除start **********/
    // 新增弹框
    handleCreate(val, subval){
      this.dialogStatus = 'create'
      this.addDialog = true;
      this.cancelSource()
      this.resetTemp()
    },
    // 大类改变
    fristTypeChange(code){
      this.fetchConfigSubType(code)
      this.systemForm.configSubtype = ''
    },
    // 新增确定
    creatSystem(form){
      this.$refs.systemForm.validate(valid => {
        if(valid){
          request({
            url:'/api/platform/config/add_config',
            method: 'post',
            data: form,
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success('新增成功');
              this.fetchSystemComfig();
            }else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            }else{
              this.$message.error(data.message)
            }
            this.closeSlide()
          })
        }
      })
    },
    // 编辑弹框
    handleUpdate(thirdType){
      if(this.operatePermission){
        this.dialogStatus = 'update'
        this.addDialog = true;
        this.systemForm = {
          id: thirdType.id,
          configType: thirdType.configType,
          configSubtype: thirdType.configSubtype,
          configValue: thirdType.configValue,
          configName: thirdType.configName,
          displayName: thirdType.displayName,
          readOnly: thirdType.readOnly,
          configDesc: thirdType. configDesc
        }
      }
    },
    // 编辑确定弹框
    updateSystem(form){
      this.$refs.systemForm.validate(valid => {
        if(valid){
          request({
            url:'/api/platform/config/update_config',
            method: 'post',
            data: form,
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success('编辑成功');
              this.fetchSystemComfig();
            }else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            }else{
              this.$message.error(data.message)
            }
            this.closeSlide()
          })
        }
      })
    },
    // 删除
    handDelete(id){
      if(this.operatePermission){
        this.$confirm('确定删除此参数？', '提示', {
          type: 'warning'
        }).then(() => {
          request({
            url:'/api/platform/config/del_config',
            method: 'post',
            data: {
              id: id
            },
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success('删除成功');
              this.fetchSystemComfig();
            }else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            }else{
              this.$message.error(data.message)
            }
          })
        }).catch(() => {})
      }
      this.$confirm('确定删除此参数？', '提示', {
        type: 'warning'
      }).then(() => {
        request({
          url:'/api/platform/config/del_config',
          method: 'post',
          data: {
            id: id
          },
        }).then(({data}) => {
          if(data.code == 200){
            this.$message.success('删除成功');
            this.fetchSystemComfig();
          }else if(data.code === 4010){
            this.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          }else{
            this.$message.error(data.message)
          }
        })
      }).catch(() => {})
    },
    /********* 新增、编辑、删除end **********/
    // 重置弹框内容
    resetTemp(){
      this.systemForm = {
        configType: undefined, // 参数类型
        configSubtype: undefined,  // 参数子类型
        configValue: '', // 参数值
        configName: '', // 参数名
        displayName: '',  // 参数显示值
        readOnly: 'n',  // 是否只读
        configDesc: '' // 描述
      }
    },
    // 取消验证
    cancelSource(){
      this.$refs.systemForm.resetFields()
    },

  },
  created(){
    this.fetchSystemComfig()
    this.fetchconfigType()
    this.fetchFixedParam()
  }
}
</script>

<style lang="scss" scoped>
.components-nav {
  padding-bottom: 15px;
  background: #f1f1f1;
  line-height: 28px;
}
.section-bod{
  text-align: left;
}
#tab-box{
  height: 650px;
  background-color: #ffffff;
	box-shadow: 0px 4px 3px 0px
		rgba(0, 0, 0, 0.08);
  border-radius: 5px;
  overflow: hidden;
  .side-tab{
    width: 150px;
    height: 100%;
    padding: 25px 0 0 15px;
    border-right: 1px solid #f0f0f0;
    background: #fcfcfc;
    float: left;
    ul li{
      height: 36px;
      line-height: 36px;
      font-size: 14px;
      color: #555;
      position: relative;
      right: -1px;
      padding-right: 20px;
      text-align: right;
      border: 1px solid transparent;
      border-right: none;
      cursor: pointer;
      transition: all 0.3s;
      &.active{
        background: #fff;
        color: #4562fc;
        border-color: #f0f0f0;
        border-radius: 5px 0px 0px 5px;
      }
      &:hover{
        color: #4562fc;
      }
    }
  }
  .tab-pane-wrap{
    height: 100%;
    margin-left: 150px;
    padding: 30px;
    .tab-pane{
      display: none;
      .svg-icon{
        height: 100%;
        width: 18px;
        float: right;
      }
      &.show{
        display: block;
      }
    }
  }
  .el-form{
    text-align: left;
    .el-form-item{
      height: 40px;
      .el-form-item__label{
        font-size: 12px;
        color: #999;
      }
      .el-form-item__content{
        height: 100%;
        .el-input{
          width: 320px;
        }

      }
    }
    .el-input.is-disabled .el-input__inner{
      cursor: default;
    }
  }
}
</style>