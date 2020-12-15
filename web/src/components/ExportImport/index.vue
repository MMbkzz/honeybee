<template>
  <slide-box
    slideWidth="600px"
    :slideShow="batchExportDialog"
    @close="slideClose"
    title="批量导出"
    ref="exportSlide"
  >
    <div slot="slide-content">
      <div class="export-form">
        <el-collapse v-model="activeNames">
          <el-collapse-item title="资产模型" name="1">
            <el-transfer
              class="mt-15"
              v-model="dataAssetModels"
              filterable
              filter-placeholder="请输入资产模型名称"
              @change="chooseAssetModel"
              :left-default-checked="[]"
              :right-default-checked="[]"
              :titles="['资产模型列表', '导出模型']"
              :data="assetModelList">
            </el-transfer>
          </el-collapse-item>
          <el-collapse-item title="能力模型" name="2">
            <el-transfer
              class="mt-15"
              v-model="dataAbilityModels"
              filterable
              filter-placeholder="请输入能力模型名称"
              @change="chooseAbilityModel"
              :left-default-checked="[]"
              :right-default-checked="[]"
              :titles="['资产模型列表', '导出模型']"
              :data="abilityModelList">
            </el-transfer>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>
    <div slot="slide-footer">
      <el-button size="mini" @click="closeSlide">取消</el-button>
      <el-button type="primary" size="mini" @click="exportModel">导出</el-button>
    </div>
  </slide-box>
</template>

<script>
import SlideBox from '@/components/SlideBox'
import request from '@/utils/request'
import { funDownload } from '@/utils/index'
export default {
  name: 'export-import',
  components: {
    SlideBox
  },
  data(){
    return {
      dataAssetModels: [],
      dataAbilityModels: [],
      activeNames: ['1', '2'],
      assetModelList: [],
      abilityModelList: []
    }
  },
  props: {
    batchExportDialog:{
      type: Boolean,
      default: false
    }
  },
  mounted() {
  },
  watch: {
    batchExportDialog(newVal, oldVal){
      if(newVal){
        // 调用接口获取全部模型
        request({
          url: '/api/service/model/query_model',
          method: 'get',
          params: {
            typeCode: 'asset'
          }
        }).then(({ data }) => {
          this.assetModelList = []
          data.data.list.forEach((item, i) => {
            this.assetModelList.push({
              key: i, // 所有模型index， 用于之后的Arr[0]
              label: item.modelName,
              id: item.id,
              name: item.modelName,
              model: item,
            })
          })
        })
        request({
          url: '/api/service/model/query_model',
          method: 'get',
          params: {
            typeCode: 'ability'
          }
        }).then(({ data }) => {
          this.abilityModelList = []
          data.data.list.forEach((item, i) => {
            this.abilityModelList.push({
              key: i, // 所有模型index， 用于之后的Arr[0]
              label: item.modelName,
              id: item.id,
              name: item.modelName,
              model: item,
            })
          })
        })
      }
    }
  },
  methods: {
    closeSlide(){
      this.$refs.exportSlide.close()
      this.dataAssetModels = []
      this.dataAbilityModels = []
    },
    slideClose(){
      this.$emit('close')
    },
    exportModel(){
      const modelArr = []
      let count = 0
      this.dataAssetModels.forEach(key => {
        let obj = this.assetModelList.find(item => item.key === key)
        request({
          url: '/api/service/model/get_model',
          method: 'get',
          params: {
            id: obj.model.id
          }
        }).then(({ data }) => {
          if(data.code === 200){
            modelArr.push({
              row: obj.model,
              modelData: data.data
            })
            count++
          }
        })
      })
      this.dataAbilityModels.forEach(key => {
        let obj = this.abilityModelList.find(item => item.key === key)
        request({
          url: '/api/service/model/get_model',
          method: 'get',
          params: {
            id: obj.model.id
          }
        }).then(({ data }) => {
          if(data.code === 200){
            modelArr.push({
              row: obj.model,
              modelData: data.data
            })
            count++
          }
        })
      })
      const self = this
      let len = this.dataAssetModels.length + this.dataAbilityModels.length
      window.exportTimer = setInterval(function(){
        if(count === len){
          clearInterval(window.exportTimer)
          self.$emit('handleModel', modelArr)
          // console.log(models)
          // let modelJson = JSON.stringify(models)
          // funDownload(modelJson, 'model.json')
        }
      }, 200)
    },
    chooseAssetModel(curretArr,directive,Arr){
      console.log(this.dataAssetModels)
    },
    chooseAbilityModel(curretArr,directive,Arr){
      console.log(this.dataAbilityModels)
    }
  }
}
</script>
<style lang="scss" scoped>
.export-form{
  padding: 10px 30px;
}
</style>
