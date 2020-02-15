<template>
    <Modal :title="title" v-model="value" :width="width" :styles="{top: '50px'}">
        <div class="card-search">
            <Form :model="searchParam" :label-width="80">
                <Row>
                    <Col span="7">
                        <FormItem label="系列名称">
                            <Input v-model="searchParam.skuName" placeholder="系列名称" clearable></Input>
                        </FormItem>
                    </Col>
                    <Col span="7">
                        <FormItem label="价格">
                            <Input v-model="searchParam.price" placeholder="价格" clearable></Input>
                        </FormItem>
                    </Col>
                    <Col span="7">
                        <FormItem label="重量">
                            <Input v-model="searchParam.weight" placeholder="重量" clearable></Input>
                        </FormItem>
                    </Col>
                    <Col span="2" offset="1" class=" card-search-search-btn-box" :label-width="10">
                        <Button type="primary" @click="search">搜索</Button>
                    </Col>
                </Row>
            </Form>
        </div>
        <div class="card-content">
            <div class="card-content-title">
                <Row type="flex" justify="start" class="card-content-row">
                    <Col span="12" class="card-content-add"><img @click="toSave()" src="@/assets/images/icon-add.png"/></Col>
                    <Col span="12"><img @click="deletes"src="@/assets/images/icon-delete.png"/></Col>
                </Row>
            </div>
            <div class="card-content-table">
                <Table border ref="tables" :loading="loading" :columns="columns" :data="listData"></Table>
            </div>
            <div class="card-content-pages">
                <Page :total="totalCount" show-elevator show-total :page-size="searchParam.pageSize" @on-change='change' @on-page-size-change='change'></Page>
            </div>
        </div>
        <stock-save ref="stockSave"  @refresh='initList'></stock-save>
    </Modal>
</template>

<script>
import { mapActions } from 'vuex'
import stockSave from './stock-save'
export default {
    components: {
        stockSave
    },
    data() {
        return {
            value: false,
            title: '库存',
            loading: false,
            listData: [],
            totalCount: 0,
            searchParam: {
                pageSize: 10,
                productId: ''
            },
            columns: [
                { type: 'selection', width: 60, align: 'center' },
                { title: '序号', width:80, type: 'index', align: 'center' },
                { title: '库存名称', align: 'center', key: 'skuName', tooltip: true },
                { title: '价格', width: 120, align: 'center', key: 'price', tooltip: true },
                { title: '重量', width: 120, align: 'center', key: 'weight', tooltip: true },
                { title: '描述', align: 'center', key: 'skuDesc', tooltip: true },
                { title: '操作', align: 'center', width: 120,
                  render: (h, params) => {
                    let row = params.row;
                    return (
                        <div>
                            <Button type='warning' onClick={() => {}}>修改</Button>
                        </div>
                    )
                  }
                }
            ]
        }
    },
    computed: {
        width (){
            return document.body.clientWidth - 100
        }
    },
    methods: {
        ...mapActions(['getSkuList', 'saveProductInfo','productDetail']),
        init(id, productName, catalog3Id) {
            this.searchParam.productId = id
            this.searchParam.productName = productName
            this.searchParam.catalog3Id = catalog3Id
            this.title = productName + '的库存'
            this.initList()
            this.value = true
        },
        change (value) {
            this.searchParam.pageNum = value
            this.initList()
        },
        search(){
            this.initList()
        },
        initList(){
            this.getSkuList(this.searchParam).then(res => {
                this.listData = res.data.list
                this.totalCount = res.data.total
            })
        },
        toSave(id){
            this.$refs.stockSave.init(id ? id : null, this.searchParam);
        },
        deletes(){

        }
    }
}
</script>
<style lang="less" scoped>
  @import "~@/style/style.less";
    .card-content {
        min-height: 400px;
    }
    .card-content-title{
        height: 40px !important;
    }
</style>
