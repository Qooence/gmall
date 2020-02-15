<template>
    <Modal :title="title" v-model="value" @on-cancel="handlerCancel" :width="width" :styles="{top: '50px'}">
        <Form ref="formData" :model="formData" :label-width="100">
            <FormItem label="系列名称">
               {{formData.productName}}
            </FormItem>
            <FormItem label="商品名称" :rules="{required: true, message:'商品名称不能为空', trigger: 'blur'}" prop='skuName'>
               <Input v-model="formData.skuName" placeholder="商品名称"></Input> 
            </FormItem>
            <FormItem label="商品价格" :rules="{required: true, message:'商品价格不能为空', trigger: 'blur'}" prop='price'>
               <Input v-model="formData.price" placeholder="库存名称"></Input> 
            </FormItem>
            <FormItem label="商品重量">
               <Input v-model="formData.weight" placeholder="商品重量"></Input> 
            </FormItem>
            <FormItem label="商品描述" :rules="{required: true, message:'商品描述不能为空', trigger: 'blur'}" prop='skuDesc'>
               <Input type="textarea" :rows="4" v-model="formData.skuDesc" placeholder="商品描述"></Input> 
            </FormItem>
            <attr-select :attrList="baseAttrList" @getValue="getBaseAttrValue"></attr-select>
            <sale-attr-select :saleAttrList="saleAttrList" @getValue="getSaleAttrValue"></sale-attr-select>
            <Table border ref="imgTables" :columns="columns" :data="imgList"></Table>
        </Form>
        <div slot="footer">
            <Button @click="handlerCancel">取消</Button>
            <Button type="primary" style="margin-left: 8px" @click="handleSubmit">确认</Button>
        </div>
    </Modal>
</template>

<script>
import { mapActions } from 'vuex'
import attrSelect from './attrSelect'
import saleAttrSelect from './saleAttrSelect'
export default {
    components: {
        attrSelect,
        saleAttrSelect
    },
    computed: {
        width (){
            return document.body.clientWidth - 100
        }
    },
    data() {
        return {
            value: false,
            title: '添加',
            formData: {},
            baseAttrList: [],
            saleAttrList: [],
            imgList: [],
            columns:[
                { title: '序号', width:80, type: 'index', align: 'center' },
                { title: '图片', align: 'center', key: 'skuName', tooltip: true },
                { title: '图片名称', width: 120, align: 'center', key: 'price', tooltip: true },
                { title: '操作', align: 'center', width: 120}
            ]
        }
    },
    methods: {
        ...mapActions(['getAttrInfoList', 'getPmsProductSaleAttr','saveProductInfo', 'productDetail']),
        init(id, { productId, productName, catalog3Id }) {
            this.formData.productName = productName
            this.getBaseAttrLlist(catalog3Id)
            this.getSaleAttrList(productId)
            if(id){
                this.title = '修改'
                this.productDetail(id).then(res =>{
                    this.formData.id = res.data.id
                    this.formData.productName = res.data.productName
                    this.formData.description = res.data.description
                    this.tableData = res.data.productSaleAttrs
                })
            }else{
                this.title = '新增'
            }
            this.value = true
        },
        getBaseAttrLlist(catalog3Id){
            this.getAttrInfoList({pageSize: 100, catalog3Id: catalog3Id}).then(res => {
                if(res.data.list && res.data.list.length > 0){
                    this.baseAttrList = res.data.list
                }
            })
        },
        getSaleAttrList(productId){
            this.getPmsProductSaleAttr(productId).then(res => {
                if (res.data && res.data.length > 0) {
                    this.saleAttrList = res.data
                }
            })
        },
        handleSubmit() {
            this.$refs['formData'].validate((valid) => {
                if (valid) {
                    this.formData.productSaleAttrs = this.tableData
                    this.saveProductInfo(this.formData).then(res => {
                        this.handlerCancel()
                        this.$emit('refresh')
                        this.$Notice.success({
                            title: '提示',
                            desc: res.message
                        })
                    })
                } else {
                    this.$Notice.warning({
                        title: '提示',
                        desc: '请完善信息'
                    })
                }
            })
        },
        handlerCancel() {
            this.value = false
            this.formData = {}
            this.$refs.formData.resetFields()
        },
        getBaseAttrValue(value){
            console.log(value)
        },
        getSaleAttrValue(value){
            console.log(value)
        }
    }
}
</script>