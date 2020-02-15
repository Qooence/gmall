<template>
    <div>
      <Card class="card-search">
        <Form :model="searchParam" :label-width="80">
          <Row>
            <Col span="7">
              <FormItem label="一级标题">
                <Select v-model="searchParam.catalog1Id" placeholder="一级标题" @on-change="catalog1Change" clearable>
                    <Option v-for="item in catalogList1" :value="item.id" :key="item.id">{{ item.name }}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="二级标题">
                <Select v-model="searchParam.catalog2Id" placeholder="二级标题" @on-change="catalog2Change" clearable>
                    <Option v-for="item in catalogList2" :value="item.id" :key="item.id">{{ item.name }}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="7">
              <FormItem label="三级标题">
                <Select v-model="searchParam.catalog3Id" placeholder="三级标题" clearable>
                    <Option v-for="item in catalogList3" :value="item.id" :key="item.id">{{ item.name }}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="2" offset="1" class=" card-search-search-btn-box" :label-width="10">
              <Button type="primary" @click="search">搜索</Button>
            </Col>
          </Row>
        </Form>
      </Card>
      <Card class="card-content">
        <p slot="title" class="card-content-title">
          <Row type="flex" justify="start" class="card-content-row">
            <Col span="12" class="card-content-add"><img @click="toSave()" src="@/assets/images/icon-add.png"/></Col>
            <Col span="12"><img @click="deletes"src="@/assets/images/icon-delete.png"/></Col>
          </Row>
        </p>
        <div class="card-content-table">
          <Table border ref="selection" :loading="loading" :columns="colData" :data="listData" @on-selection-change="onChange">
          </Table>
        </div>
        <div class="card-content-pages">
          <Page :total="totalCount" show-elevator show-total :page-size="searchParam.limit" @on-change='change' @on-page-size-change='change'></Page>
        </div>
      </Card>
      <product-save ref="productSave"  @refresh='initData'></product-save>
      <stock ref="stock"  @refresh='initData'></stock>
    </div>
</template>
<script>
import { mapActions } from 'vuex'
import productSave from './product-save'
import stock  from '../stock'
export default {
  name: 'attr',
  components: {
    productSave,
    stock
  },
  data () {
    return {
      searchParam: {
        // pageNum: 1,
        pageSize: 10
      },
      loading: false,
      catalogList1: [],
      catalogList2: [],
      catalogList3: [],
      colData: [
        {
          type: 'expand',
          align: 'center',
          width: 50,
          render: (h, params) => {
            let row = params.row
            if(row.productSaleAttrs && params.row.productSaleAttrs.length > 0) {
              let arr = []
              row.productSaleAttrs.forEach(item => {
                if(item.saleAttrValues && item.saleAttrValues.length > 0) {
                  let attrValue = []
                  item.saleAttrValues.filter(value => {
                    attrValue.push((<Tag type="border" color="success">{value.saleAttrValueName}</Tag>))
                  })
                  arr.push(
                    <div>
                      <Row class="expand-row">
                        <Col span="8">
                          <span class="expand-key">{item.saleAttrName}</span>
                        </Col>
                        <Col span="16">
                            {attrValue}
                        </Col>
                      </Row>
                    </div>
                  )
                }
              })
              return arr
            }
          }
        },
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '序号',
          type: 'index',
          width: 80,
          align: 'center'
        },
        {
          title: '系列名称',
          key: 'productName',
          tooltip: true,
          align: 'center'
        },
        {
          title: '销售属性',
          align: 'center',
          render: (h, params) => {
            if(params.row.productSaleAttrs && params.row.productSaleAttrs.length > 0){
              let arr = []
              params.row.productSaleAttrs.forEach(item => {
                arr.push(<Tag type="border" color="success">{item.saleAttrName}</Tag>)
              })
              return arr;
            }
          }
        },
        {
          title: '描述',
          align: 'center',
          tooltip: true,
          key: 'description'
        },
        {
          title: '操作',
          width: 200,
          align: 'center',
          render: (h, params) => {
            let row = params.row;
            return (
              <div>
                <Button type='success' onClick={() => this.toStock(row.id, row.productName)} style="margin-right:5px">库存</Button>
                <Button type='warning' onClick={() => this.toSave(row.id)}>修改</Button>
              </div>
            )
          }
        }
      ],
      listData: [],
      totalCount: 0,
      selectArry: []
    }
  },
  methods: {
    ...mapActions(['getCatalog1', 'getCatalog2', 'getCatalog3', 'getProductInfoList', 'deleteProductInfos']),
    search () {
      if(this.searchParam.catalog3Id){
        // this.searchParam.pageNum = 1
        this.initData()
      }else{
        this.$Notice.warning({
          title:'提示',
          desc: '请完善到三级标题'
        })
      }
    },
    change (value) {
      this.searchParam.pageNum = value
      this.initData()
    },
    catalog1Change(value){
      this.$set(this.searchParam, 'catalog3Id', '')
      this.catalogList3.length = 0
      if(value){
        this.getCatalog2List(value)
      }else{
        this.$set(this.searchParam, 'catalog2Id', '')
        this.catalogList2.length = 0
      }
    },
    catalog2Change(value){
      if(value){
        this.getCatalog3List(value);
      }else{
        this.$set(this.searchParam, 'catalog3Id', '')
        this.catalogList3.length = 0
      }
    },
    getCatalog1List(){
      this.getCatalog1().then(data => {
        this.catalogList1 = data
      })
    },
    getCatalog2List(calalog1Id){
      this.getCatalog2(calalog1Id).then(data => {
        this.catalogList2 = data
      })
    },
    getCatalog3List(calalog2Id){
      this.getCatalog3(calalog2Id).then(data => {
        this.catalogList3 = data
      })
    },
    initData() {
      this.getProductInfoList(this.searchParam).then(res => {
        this.listData = res.data.list
        this.totalCount = res.data.total
      })
    },
    toSave(id){
      if(this.searchParam.catalog3Id){
        this.$refs.productSave.init(id ? id : null,this.searchParam);
      }else{
        this.$Notice.warning({
          title:'提示',
          desc: '请完善到三级标题'
        })
      }
    },
    toStock(id, productName){
      this.$refs.stock.init(id, productName, this.searchParam.catalog3Id);
    },
    onChange (value) {
      this.selectArry = value
    },
    deletes() {
      if (this.selectArry.length > 0) {
        this.$Modal.confirm({
          title: '提示',
          content: '确认进行删除操作吗？',
          onOk: () => {
            this.deleteProductInfos(this.selectArry.map(item => item.id)).then((res) => {
              this.initData()
              this.$Notice.success({
                title: '提示',
                desc: res.message
              })
            })
          },
          onCancel: () => {
            this.selectArry.length = 0
            this.$refs.selection.selectAll(false)
          }
        })
      } else {
        this.$Notice.warning({
          title: '警告',
          desc: '请至少选择一项'
        })
      }
    }
  },
  mounted () {
    this.getCatalog1List()
  }
}
</script>
<style lang="less" scoped>
  @import "~@/style/style.less";
  .expand-row{
      margin-bottom: 16px;
  }
</style>
