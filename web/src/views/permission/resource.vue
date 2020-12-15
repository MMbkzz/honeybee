<template>
<div>
  <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">平台资源管理</h2>
        </div>
      </div>
  </nav>
  <div class="section-bod">
    <cr-loading v-show="loading"></cr-loading>
    <fold-table
      v-show="!loading"
      :data="data"
      :allExpand="false"
      :operatePermission="operatePermission"
      @switchChange="switchChange"
      @edit="(val) => nodeEdit(val)"
      @delete="(val) => nodeDelete(val.id)">
    </fold-table>
  </div>
  <!-- <div class="resource-tree"> -->
    <!-- <div class="header"> -->
      <!--新增 开发用-->
      <!-- <el-button type="primary" size="mini" @click="addParentResource">新增一级资源</el-button> -->
    <!-- </div> -->
    <!-- <div class="main-content">
      <div class="tree-header">
        <div>资源名称</div>
        <div>操作</div>
        <div>状态</div>
      </div>
      <el-tree class="tree-container"
        :data="data"
        :props="defaultProps"
        node-key="id"
        default-expand-all
        :expand-on-click-node="false">
        <div class="custom-tree-node" slot-scope="{ node, data }">
            <span>{{ node.label }}</span>
            <span class="custom-tree-node-option">
                <crland-switch style="margin-right: 80px;" :objBind="data" :status="statusParse(data.status)" :offText="'停用'" @handleClick="switchChange"></crland-switch>

                <span @click="() => nodeEdit(data)" style="margin-right: 10px;">
                  <img src="../../assets/tdspic/edit.png" alt="删除">
                </span>
                <span @click="() => nodeDelete(data.id)" style="margin-right: 116px;">
                  <img src="../../assets/tdspic/delete.png" alt="编辑">
                </span>

            </span>
        </div>
      </el-tree>
    </div> -->

    <!--新增资源弹框-->
    <!-- <el-dialog title="新增资源" :visible.sync = "createDialog" width="550px">
      <el-form ref="createForm" :rules="resourceRules" :model="resource" label-width="80px" size="mini" label-position="right">
        <el-form-item label="编码" prop="code" style="width: 86%">
          <el-input v-model="resource.code"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="name" style="width: 86%">
          <el-input v-model="resource.name"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="category_id" style="width: 86%">
          <el-select v-model="resource.category_id" filterable placeholder="请选择分类" style="width: 100%;">
            <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="descr" style="width: 86%">
          <el-input type="textarea" v-model="resource.descr"></el-input>
        </el-form-item>
        <el-form-item label="路径名" prop="attr1" style="width: 86%">
          <el-input v-model="resource.attr1"></el-input>
        </el-form-item>
        <el-form-item label="图标名" prop="attr2" style="width: 86%">
          <el-input v-model="resource.attr2"></el-input>
        </el-form-item>
        <el-form-item label="序号" prop="attr3" style="width: 86%">
          <el-input v-model="resource.attr3"></el-input>
        </el-form-item>
        <el-form-item label="是否隐藏" prop="attr4" style="width: 86%">
          <el-input v-model="resource.attr4"></el-input>
        </el-form-item>
        <el-form-item label="属性5" prop="attr5" style="width: 86%">
          <el-input v-model="resource.attr5"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialog = false;$refs.createForm.resetFields()">取消</el-button>
        <el-button type="primary" @click="addResource">确认</el-button>
      </div>
    </el-dialog> -->
    <!--更新资源弹框-->
    <!-- <el-dialog title="更新资源" :visible.sync = "editDialog" width="550px">
      <el-form ref="editForm" :rules="editRules" :model="eResource" label-width="80px" label-position="right">
        <el-form-item label="编码" prop="code" style="width: 86%">
          <el-input v-model="eResource.code"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="name" style="width: 86%" class="mt-15">
          <el-input v-model="eResource.name"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="descr" style="width: 86%" class="mt-15">
          <el-input type="textarea" v-model="eResource.descr"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="category_id" style="width: 86%" class="mt-15">
          <el-select v-model="eResource.category_id" filterable placeholder="请选择分类" style="width: 100%;">
            <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status" style="width: 86%" class="mt-15">
          <el-select v-model="eResource.status" placeholder="请选择" style="width: 100%;">
            <el-option v-for="item in statuses" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="路径名" prop="attr1" style="width: 86%" class="mt-15">
          <el-input v-model="eResource.attr1"></el-input>
        </el-form-item>
        <el-form-item label="图标名" prop="attr2" style="width: 86%" class="mt-15">
          <el-input v-model="eResource.attr2"></el-input>
        </el-form-item>
        <el-form-item label="序号" prop="attr3" style="width: 86%" class="mt-15">
          <el-input v-model="eResource.attr3"></el-input>
        </el-form-item>
        <el-form-item label="是否隐藏" prop="attr4" style="width: 86%" class="mt-15">
          <el-input v-model="eResource.attr4"></el-input>
        </el-form-item>
        <el-form-item label="属性5" prop="attr5" style="width: 86%" class="mt-15">
          <el-input v-model="eResource.attr5"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialog = false;$refs.editForm.resetFields()">取消</el-button>
        <el-button type="primary" @click="editResource">确认</el-button>
      </div>
    </el-dialog> -->
    <!-- 滑框 -->

  <!-- </div> -->
  <slide-box slideWidth="450px" :slideShow="editDialog" @close="slideClose" title="更新资源" ref="slideBox">
    <div slot="slide-content">
      <el-form ref="editForm" :rules="editRules" :model="eResource" label-width="80px" label-position="right">
        <el-form-item label="编码" prop="code" style="width: 86%">
          <el-input v-model="eResource.code"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="name" style="width: 86%" class="mt-15">
          <el-input v-model="eResource.name"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="descr" style="width: 86%" class="mt-15">
          <el-input type="textarea" v-model="eResource.descr"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="category_id" style="width: 86%" class="mt-15">
          <el-select v-model="eResource.category_id" filterable placeholder="请选择分类" style="width: 100%;">
            <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status" style="width: 86%" class="mt-15">
          <el-select v-model="eResource.status" placeholder="请选择" style="width: 100%;">
            <el-option v-for="item in statuses" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="路径名" prop="attr1" style="width: 86%" class="mt-15">
          <el-input v-model="eResource.attr1"></el-input>
        </el-form-item>
        <el-form-item label="图标名" prop="attr2" style="width: 86%" class="mt-15">
          <el-input v-model="eResource.attr2"></el-input>
        </el-form-item>
        <el-form-item label="序号" prop="attr3" style="width: 86%" class="mt-15">
          <el-input v-model="eResource.attr3"></el-input>
        </el-form-item>
        <el-form-item label="是否隐藏" prop="attr4" style="width: 86%" class="mt-15">
          <el-input v-model="eResource.attr4"></el-input>
        </el-form-item>
        <el-form-item label="属性5" prop="attr5" style="width: 86%" class="mt-15">
          <el-input v-model="eResource.attr5"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <span slot="slide-footer">
      <el-button :disabled="submitBtnDisabled" size="mini" type="primary" @click="editResource">保存</el-button>
      <el-button size="mini" @click="closeSlide">取消</el-button>
    </span>
  </slide-box>
</div>

</template>

<script>
  import CrlandSwitch from '@/components/CrlandSwitch'
  import FoldTable from '@/components/FoldTable'
  import SlideBox from '@/components/SlideBox'
  import CrLoading from '@/components/CrLoading'
  import { getResourceTree, getTypes, addResource, editResource, deleteResource, startResource, stopResource, checkResourceCode } from '@/api/resource'
  export default {
    components:{
      CrlandSwitch,
      SlideBox,
      FoldTable,
      CrLoading
    },
    data() {
      return {
        operatePermission: false,
        submitBtnDisabled: false,
        loading: true,
        data: [],
        preName: '',
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        createDialog: false,
        editDialog: false,
        resource: {
          category_id: '',
          name: '',
          code: '',
          descr: '',
          status: '1',
          attr1: '',
          attr2: '',
          attr3: '',
          attr4: '',
          attr5: '',
          parent_id: ''
        },
        eResource: {
          id: '',
          category_id: '',
          name: '',
          code: '',
          descr: '',
          status: '',
          attr1: '',
          attr2: '',
          attr3: '',
          attr4: '',
          attr5: ''
        },
        types: [],
        statuses: [
          { id: '0', name: '禁用' },
          { id: '1', name: '启用' }
        ],
        editRules: {
          category_id: [
            { required: true, message: '请选择分类', trriger: 'blur' }
          ],
          name: [
            { required: true, message: '请填写名称', trriger: 'blur' },
            { max: 20, message: '最多为20位', trriger: 'blur' }
          ],
          code: [
            { required: true, message: '请输入资源编码', trriger: 'blur' },
            { pattern: /^[0-9a-zA-Z_]+$/, message: '编码为数字，字母，下划线组成', trriger: 'blur' },
            { validator: (rule, val, cb) => {
              if (this.editDialog) {
                checkResourceCode(val, this.eResource.id).then(({ data }) => {
                  if (data.code === 200) {
                    if (data.data.result) {
                      cb(new Error('资源编码已存在，请重新输入'))
                    } else {
                      cb()
                    }
                  }
                })
              } else {
                checkResourceCode(val).then(({ data }) => {
                  if (data.code === 200) {
                    if (data.data.result) {
                      cb(new Error('资源编码已存在，请重新输入'))
                    } else {
                      cb()
                    }
                  }
                })
              }
            }, trigger: 'blur' }
          ],
          descr: [
            { required: true, message: '请填写描述', trriger: 'blur' }
          ],
          status: [
            { required: true, message: '请选择状态', trriger: 'blur' }
          ]
        },
        resourceRules: {
          category_id: [
            { required: true, message: '请选择分类', trriger: 'blur' }
          ],
          name: [
            { required: true, message: '请填写名称', trriger: 'blur' },
            { max: 20, message: '最多为20位', trriger: 'blur' }
          ],
          code: [
            { required: true, message: '请输入资源编码', trriger: 'blur' },
            { pattern: /^[0-9a-zA-Z_]+$/, message: '编码为数字，字母，下划线组成', trriger: 'blur' },
            { validator: (rule, val, cb) => {
              if (this.editDialog) {
                checkResourceCode(val, this.eResource.id).then(({ data }) => {
                  if (data.code === 200) {
                    if (data.data.result) {
                      cb(new Error('资源编码已存在，请重新输入'))
                    } else {
                      cb()
                    }
                  }
                })
              } else {
                checkResourceCode(val).then(({ data }) => {
                  if (data.code === 200) {
                    if (data.data.result) {
                      cb(new Error('资源编码已存在，请重新输入'))
                    } else {
                      cb()
                    }
                  }
                })
              }
            }, trigger: 'blur' }
          ],
          descr: [
            { required: true, message: '请填写描述', trriger: 'blur' }
          ],
          status: [
            { required: true, message: '请选择状态', trriger: 'blur' }
          ]
        }
      }
    },
    mounted() {
      // console.log(this.data)
      this.$store.getters.userInfo.menu.forEach(item => {
      item.children.forEach(subItem => {
        if(subItem.name === '平台资源管理'){
          this.operatePermission = subItem.operationIds.indexOf('3') !== -1
          console.log(subItem.name+'页面操作权限',this.operatePermission)
        }
      })
    })
    },
    methods: {
      // 关闭滑框
      closeSlide(){
        this.$refs.slideBox.close()
      },
      slideClose(){
        this.editDialog = false
      },
      nodeEdit(data) {
        const { category_id, name, code, descr, status, id, attr1, attr2, attr3, attr4, attr5 } = data
        this.eResource = { category_id, name, code, descr, status, id, attr1, attr2, attr3, attr4, attr5 }
        this.editDialog = true
      },
      nodeAdd(id) {
        this.createDialog = true
        this.resource.parent_id = id
        // 重置表单
        if (this.$refs.createForm) {
          this.$refs.createForm.resetFields()
        }
      },
      addParentResource() {
        this.createDialog = true
        this.resource.parent_id = '0'
        // 重置表单
        if (this.$refs.createForm) {
          this.$refs.createForm.resetFields()
        }
      },
      nodeDelete(id) {
        this.$confirm('确定删除此资源及其下资源?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteResource(id).then(({ data }) => {
            if (data.code === 200) {
              this.$message.success('删除成功')
              this.fetchTree()
            } else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            }else {
              this.$message.error(data.message)
            }
          })
        })
      },
      fetchTree() {
        this.loading = true
        getResourceTree().then(({ data }) => {
          const self = this
          setTimeout(function(){
            self.loading = false
            console.log(data.data)
            self.data = data.data
          },250)
        })
      },
      editResource() {
        this.$refs.editForm.validate(valid => {
          if (valid) {
            this.submitBtnDisabled = true
            editResource(this.eResource).then(({ data }) => {
              if (data.code === 200) {
                this.$message.success('更新资源成功')
                this.$refs.editForm.resetFields()
                this.closeSlide()
                this.fetchTree()
              } else if(data.code === 4010){
                this.$store.dispatch('LogOut').then(() => {
                  location.reload()
                })
              }else {
                this.$message.error(data.message)
              }
            })
          }
        })
      },
      addResource() {
        this.$refs.createForm.validate(valid => {
          if (valid) {
            addResource(this.resource).then(({ data }) => {
              if (data.code === 200) {
                this.$message.success('新增资源成功')
                this.$refs.createForm.resetFields()
                this.createDialog = false
                this.fetchTree()
              }else if(data.code === 4010){
                this.$store.dispatch('LogOut').then(() => {
                  location.reload()
                })
              } else {
                this.$message.error(data.message)
              }
            })
          }
        })
      },

      // 开关函数
      switchChange(row){
        if(row.state){
          startResource(row.item.id).then(({ data }) => {
            if (data.code === 200) {
              // item.status = '1'
              this.$message.success('启用成功')
            }else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            } else {
              this.$message.error('启用资源失败')
            }
          })
        }else{
          stopResource(row.item.id).then(({ data }) => {
          if (data.code === 200) {
            // item.status = '0'
            this.$message.success('禁用成功')
          } else {
            this.$message.error('停用资源失败')
          }
        })
        }
      },
      // 状态解析
      statusParse(filed){
        if(filed){
          if(filed == '1'){
            return true
          }else {
            return false
          }
        }
      },
      /*  handleNodeClick(data) {
        // console.log(data);
      },
      handleContextmenu(event, data, node, tree) {
        alert('66')
      }, */
      /* start(item) {
        startResource(item.id).then(({ data }) => {
          if (data.code === 200) {
            item.status = '1'
            this.$message.success('启用成功')
          } else {
            this.$message.error('启用资源失败')
          }
        })
      }, */
      /* stop(item) {
        stopResource(item.id).then(({ data }) => {
          if (data.code === 200) {
            item.status = '0'
            this.$message.success('禁用成功')
          } else {
            this.$message.error('禁用资源失败')
          }
        })
      }, */
    },
    filters: {
      status(val) {
        if (val === '0') {
          return '禁用'
        } else if (val === '1') {
          return '启用'
        }
        return ''
      }
    },
    created() {
      this.fetchTree()
      getTypes().then(({ data }) => {
        if (data.code === 200) {
          this.types = data.data.data
        }
      })
    },

  }
</script>

<style lang="scss" scoped>
.resource-tree {
  height: 100%;
	.resource-tree {
		width: 400px;
		border: 1px solid #DDDDDD;
  }
  .el-tree-node__content{
    span{
      font-size: 12px;
    }
  }

	.custom-tree-node {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-size: 14px;
		padding-right: 8px;
	}
  .header {
    height: 40px;
    line-height: 40px;
    text-align: right;
    padding-right: 10px;
  }
  .main-content {
    height: calc(100% - 50px);
    box-shadow: 0px 2px 1px 0px rgba(0, 0, 0, 0.08);
    border-radius: 5px;
    overflow-y: auto;
    margin-top: 0;
    margin-bottom: 50px;
    .tree-header {
      font-size: 14px;
      overflow: hidden;
      background: #fcfcfc;
      line-height: 54px;
      border-bottom: 1px solid #ccc;
      padding: 0 10px;
      div{
        color: #ccc;
      }
      div:nth-of-type(1) {
        float: left;
      }
      div:nth-of-type(2) {
        float: right;
        width: 160px;
      }
      div:nth-of-type(3) {
        float: right;
        width: 130px;
      }
    }
  }

}
</style>