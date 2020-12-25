<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">数据资产地图</h2>
        </div>
      </div>
    </nav>
    <section class="components-container map-content">
      <cr-loading v-show="loading"></cr-loading>
      <div v-show="!loading" id="treeChart" style="width: 100%; height: 600px;"></div>
    </section>
  </div>
</template>

<script>
import request from '@/utils/request'
import CrLoading from '@/components/CrLoading'
export default {
  components:{
    CrLoading
  },
  data() {
    return {
      loading: true,
      color: [
        {
          name: 'business.png',
          style: '#76c80e'
        },{
          name: 'dataSharing.png',
          style: '#ba9dd3'
        },{
          name: 'engineering.png',
          style: '#ff9500'
        },{
          name: 'house.png',
          style: '#1fbba6'
        },{
          name: 'officeBuilding.png',
          style: '#47b4d8'
        },{
          name: 'tecPlatForm.png',
          style: '#eae03b'
        },{
          name: 'zhaocai.png',
          style: '#ff5e3a'
        }
      ],
      assetMap : {
        categories:[
          {name: "0"}, //关系网类别
          {name: "1"},
          {name: "2"}
        ],
        nodes: [],
        links: []
      },
      globalSeriesData: [] //用来存放被收起的某节点的子节点
    }
  },
  mounted(){
    $('#treeChart').css('width',$('.components-container').width()+'px')
    // this.drawLine();
    this.fetchAssetMap();
  },
  methods: {
    fetchAssetMap(){
      request({
        url: '/api/dataasset/map/query_map',
        methods: 'get'
      }).then(({data}) => {
        this.loading = false
        if(data.code === 200){
          // 复制所有节点
          for(var j=0; j<data.data.sides.length; j++){
            this.assetMap.links.push(
              {
                source: data.data.sides[j].source.toString(),
                target: data.data.sides[j].target.toString()
              }
            )
            // 跟级线条颜色
            if(data.data.sides[j].source == 0){
              this.assetMap.links[j].lineStyle = {
                normal: {
                  color: '#4562fc'
                }
              }
            }
          }
          for(var i=0; i<data.data.nodes.length; i++){
            this.assetMap.nodes.push(
              {
                id: data.data.nodes[i].id.toString(),
                parentId: data.data.nodes[i].parentId,
                category: data.data.nodes[i].category,
                nameTitle: data.data.nodes[i].name,
                symbol: data.data.nodes[i].symbol,
                value: data.data.nodes[i].value,
                sourceName: data.data.nodes[i].sourceName,
                statusCode: data.data.nodes[i].statusCode == 'enabled'? '启用':'禁用',
                invokeCount: data.data.nodes[i].invokeCount,
                responseAvg: data.data.nodes[i].responseAvg,
                hidden: false, // 本身隐藏与否
                childHidden: false  // 判断展开还是折叠
              }
            )
            // 图标大小
            if(data.data.nodes[i].category == 0){
              this.assetMap.nodes[i].symbolSize = '100'
              this.assetMap.nodes[i].parentId = '-1'
              this.assetMap.nodes[i].symbol = 'image://static/images/assetTitle.png'
              this.assetMap.nodes[i].nameTitle = ''
            }else if(data.data.nodes[i].category == 1){
              this.assetMap.nodes[i].symbolSize = '30'
            }else if(data.data.nodes[i].category == 2){
              this.assetMap.nodes[i].symbolSize = '20'
            }else if(data.data.nodes[i].category == 3){
              this.assetMap.nodes[i].symbolSize = '14'
            }
          }
          // 第一子类的颜色与图片
          let category1 = this.assetMap.nodes.filter(function(cur, index, arr){
            return cur.category == 1
          })
          let th = this
          category1.map(function(cur, index, arr){
            for(var j=0; j<th.color.length; j++){
              if(th.color[j].name == cur.symbol){
                cur.itemStyle = {
                  normal: {
                    color: th.color[j].style
                  }
                }
              }
            }
            cur.symbol = 'image://static/images/' + cur.symbol
          })
          // 第二类线条颜色
          for(var k=0; k<this.assetMap.links.length; k++){
            for(var kk=0; kk<category1.length; kk++){
              if(this.assetMap.links[k].source == category1[kk].id){
                this.assetMap.links[k].lineStyle = category1[kk].itemStyle
              }
            }
          }
          // 第二子类的颜色
          let category2 = this.assetMap.nodes.filter(function(cur, index, arr){
            return cur.category == 2
          })
          for(var k=0; k<category2.length; k++){
            let flag = '';
            for(var n=0; n<this.assetMap.links.length; n++){
              if(category2[k].id == this.assetMap.links[n].target){
                flag = this.assetMap.links[n].source
              }
            }
            category2[k].itemStyle = category1.filter(function(cur, index, arr){
              return cur.id == flag
            })[0].itemStyle
          }
          // 第三类线条颜色
          for(var k=0; k<this.assetMap.links.length; k++){
            for(var kk=0; kk<category2.length; kk++){
              if(this.assetMap.links[k].source == category2[kk].id){
                this.assetMap.links[k].lineStyle = category2[kk].itemStyle
              }
            }
          }
          // 第三子类的颜色
          let category3 = this.assetMap.nodes.filter(function(cur, index, arr){
            return cur.category == 3
          })
          for(var k=0; k<category3.length; k++){
            let flag = '';
            for(var n=0; n<this.assetMap.links.length; n++){
              if(category3[k].id == this.assetMap.links[n].target){
                flag = this.assetMap.links[n].source
              }
            }
            category3[k].itemStyle = category2.filter(function(cur, index, arr){
              return cur.id == flag
            })[0].itemStyle
          }
          // 第四类线条颜色
          for(var k=0; k<this.assetMap.links.length; k++){
            for(var kk=0; kk<category3.length; kk++){
              if(this.assetMap.links[k].source == category3[kk].id){
                this.assetMap.links[k].lineStyle = category3[kk].itemStyle
              }
            }
          }
          // 第四子类的颜色
          let category4 = this.assetMap.nodes.filter(function(cur, index, arr){
            return cur.category == 4
          })
          for(var k=0; k<category4.length; k++){
            let flag = '';
            for(var n=0; n<this.assetMap.links.length; n++){
              if(category4[k].id == this.assetMap.links[n].target){
                flag = this.assetMap.links[n].source
              }
            }
            category4[k].itemStyle = category3.filter(function(cur, index, arr){
              return cur.id == flag
            })[0].itemStyle
          }

          this.drawTree();
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
           location.reload()
         })
        }else{
          // this.$message.error(data.message)
        }
      })
    },
    drawTree() {
      let treeChart = this.$echarts.init(document.getElementById('treeChart'))
      let mapNodes = this.assetMap.nodes
      let mapLinks = this.assetMap.links
      let renderNodes = []
      defaultFoldLayer('4')
      getNodes()
      var globalSeriesData = []
      let option = {
        tooltip: {
          trigger: 'item',
          textStyle: {
            color: '#666'
          },
          borderColor: '#666',
          borderWidth: 1,
          backgroundColor: 'rgba(255,255,255,.5)',
          formatter: function(p){
            if(p.data.sourceName){
              return '数据源名称：'+p.data.sourceName + '<br/>'+
                      '状态：'+ p.data.statusCode+ '<br/>'+
                      '累计调用次数：'+ p.data.invokeCount.toLocaleString()+ '<br/>'+
                      '平均响应时长(s)：'+ p.data.responseAvg.toLocaleString()
            }
          }
        },
        animationDuration: 1000,
        animationEasingUpdate: 'quinticInOut',
        series: [{
          type: 'graph',
          layout: 'force',
          draggable: true,
          roam: true,
          label: {
            normal: {
              show:true,
              position: 'top',
              formatter: function(p){
                if(p.value){
                  return p.data.nameTitle+' ('+ p.value+')'
                }else{
                  return p.data.nameTitle
                }
              }
            }
          },
          force: {
            layoutAnimation: true,
            gravity: 0.03,  //节点受到的向中心的引力因子。该值越大节点越往中心点靠拢。
            edgeLength: 100,  //边的两个节点之间的距离，这个距离也会受 repulsion。[10, 50] 。值越小则长度越长
            repulsion: 180  //节点之间的斥力因子。支持数组表达斥力范围，值越大斥力越大。
          },
          data: renderNodes,
          links: mapLinks  //link数据
          // categories: this.assetMap.categories,  //关系网类别，可以写多组
        }]
      }
      treeChart.setOption(option)
      // 判断展开还是折叠  fold为true 为展开
      function foldJudge(id,childHidden){
        if(!childHidden){
          // 折叠
          for(var i=0; i<mapNodes.length; i++){
            // 该id的节点childHidden为true
            if(mapNodes[i].id == id){
              mapNodes[i].childHidden = true
            }
            // 该节点下一级hidden为false
            if(mapNodes[i].parentId == id){
              mapNodes[i].hidden = true
              mapNodes[i].childHidden = true
              for(var ii=0; ii<mapNodes.length; ii++){
                // 该节点第二级hidden为false
                if(mapNodes[ii].parentId == mapNodes[i].id){
                  mapNodes[ii].hidden = true
                  mapNodes[ii].childHidden = true
                  for(var iii=0; iii<mapNodes.length; iii++){
                    if(mapNodes[iii].parentId == mapNodes[ii].id){
                      mapNodes[iii].hidden = true
                      mapNodes[iii].childHidden = true
                      for(var iiii=0; iiii<mapNodes.length; iiii++){
                        if(mapNodes[iiii].parentId == mapNodes[iii].id){
                          mapNodes[iiii].hidden = true
                          mapNodes[iiii].childHidden = true
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }else{
          // 展开
          for(var i=0; i<mapNodes.length; i++){
            if(mapNodes[i].id == id){
              mapNodes[i].childHidden = false
            }
            if(mapNodes[i].parentId == id){
              mapNodes[i].hidden = false
            }
          }
        }
        getNodes()
      }
      // 过滤函数 获取renderNodes
      function getNodes(){
        for(var i=0; i<mapNodes.length; i++){
          if(mapNodes[i].hidden === false){
            renderNodes.push(
              mapNodes[i]
            )
          }
        }
      }
      // 默认折叠第几层
      function defaultFoldLayer(num){
        for(var i=0; i<mapNodes.length; i++){
          // console.log(mapNodes[i].nameTitle, mapNodes[i].category, mapNodes[i].hidden, mapNodes[i].childHidden)
          if(mapNodes[i].category == num-1){
            mapNodes[i].childHidden = true
          }
          if(mapNodes[i].category >= num){
            mapNodes[i].hidden = true
          }
        }
      }

      treeChart.on('click', function(params){
        // console.log(params)
        renderNodes = []
        if(params.dataType === "node"){
          foldJudge(params.data.id, params.data.childHidden)
        }else{
          return;
        }
        option.series[0].data = renderNodes;
        treeChart.setOption(option);
      })
      /* treeChart.on('click', function(params){
        if (params.dataType === "node") {
          var deletedFlag = false; // 标记点击的此节点是否存在子节点,若不存在则说明可能在上次的操作中已经删除,这时就需要尝试把之前删除的节点重新添加进去
          for (var i = mapNodes.length - 1; i >= 0; i--) {
            if (mapNodes[i].parentId == params.data.id) {
              if (mapNodes[i].parentId != mapNodes[i].id) { //排除删除根元素的可能
                deletedFlag = true;
                for (var ii = mapNodes.length - 1; ii >= 0; ii--) { //删除第一级节点的子节点
                  if (mapNodes[ii].parentId == mapNodes[i].id) {
                    for (var iii = mapNodes.length - 1; iii >= 0; iii--) { //删除第二级节点的子节点
                      if (mapNodes[iii].parentId == mapNodes[ii].id) {
                        globalSeriesData.push(mapNodes[iii]);
                        mapNodes.splice(iii, 1);
                      }
                    }
                    globalSeriesData.push(mapNodes[ii]);
                    mapNodes.splice(ii, 1);
                  }
                }
                globalSeriesData.push(mapNodes[i]);
                mapNodes.splice(i, 1); //删除该元素的第一级子节点,最多需删除三级
              }
            }
          }
          if (!deletedFlag) { //这种情况下需要恢复该节点的子节点
            var nodeChildren = []; //存放本次恢复的数据,然后将它们从globalSeriesData中删除
            for (var n = globalSeriesData.length - 1; n >= 0; n--) {
              if (params.data.id == globalSeriesData[n].parentId) { //显示该节点第一级子节点
                mapNodes.push(globalSeriesData[n]);
                nodeChildren.push(globalSeriesData[n]);
                for (var nn = globalSeriesData.length - 1; nn >= 0; nn--) {
                  if (globalSeriesData[n].id == globalSeriesData[nn].parentId) { //显示该节点第二级子节点
                    mapNodes.push(globalSeriesData[nn]);
                    nodeChildren.push(globalSeriesData[nn]);
                    for (var nnn = globalSeriesData.length - 1; nnn >= 0; nnn--) {
                      if (globalSeriesData[nn].id == globalSeriesData[nnn].parentId) { //显示该节点第三级子节点
                        mapNodes.push(globalSeriesData[nnn]);
                        nodeChildren.push(globalSeriesData[nnn]);
                      }
                    }
                  }
                }
              }
            }
            if (nodeChildren.length > 0) {
              for (var s = 0; s < nodeChildren.length; s++) {
                for (var n = globalSeriesData.length - 1; n >= 0; n--) {
                  if (nodeChildren[s].id == globalSeriesData[n].id) {
                      globalSeriesData.splice(n, 1);
                  }
                }
              }
            }
          }
          option.series[0].data = mapNodes;
          treeChart.setOption(option);
        }
      }) */
    }
  }
}
</script>

<style lang="scss" scoped>
.tree-pic {
  margin-top: 40px;
}
.map-content{
  height: 100%;
  background-color: #ffffff;
	box-shadow: 0px 2px 1px 0px rgba(0, 0, 0, 0.08);
	border-radius: 5px;
}
</style>