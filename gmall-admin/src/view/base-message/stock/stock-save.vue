<template>
    <Modal :title="title" v-model="value" @on-cancel="handlerCancel" :width="width" :styles="{top: '50px'}">
        <Form ref="formData" :model="formData" :label-width="100">
            <FormItem label="系列名称">
               {{formData.productName}}
            </FormItem>
            <FormItem label="商品名称" :rules="{required: true, message:'商品名称不能为空', trigger: 'blur'}" prop='skuName'>
               <Input v-model="formData.skuName" placeholder="商品名称"></Input> 
            </FormItem>
            <FormItem label="商品价格" :rules="{required: true, validator: this.validatePrice, trigger: 'blur'}" prop='price'>
               <Input v-model="formData.price" placeholder="商品价格"></Input> 
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
                { title: '图片名称', width: 120, align: 'center', key: 'pictrue', tooltip: true },
                { title: '操作', align: 'center', width: 120}
            ]
        }
    },
    methods: {
        ...mapActions(['getAttrInfoList', 'getPmsProductSaleAttr','saveSkuInfo', 'skuDetail']),
        init(id, { productId, productName, catalog3Id }) {
            this.formData.productName = productName
            this.formData.productId = productId
            this.formData.catalog3Id = catalog3Id
            if(id){
                this.title = '修改'
                this.skuDetail(id).then(res => {
                    this.$set(this.formData, 'id', res.data.id)
                    this.$set(this.formData, 'weight', res.data.weight)
                    this.$set(this.formData, 'skuName', res.data.skuName)
                    this.$set(this.formData, 'skuDesc', res.data.skuDesc)
                    this.$set(this.formData, 'price', res.data.price)
                    this.$set(this.formData, 'baseAttr', res.data.baseAttr)
                    this.$set(this.formData, 'saleAttr', res.data.saleAttr)
                })
            }else{
                this.title = '新增'
            }
            this.getBaseAttrLlist(catalog3Id)
            this.getSaleAttrList(productId)
            this.value = true
        },
        getBaseAttrLlist(catalog3Id){
            this.getAttrInfoList({pageSize: 100, catalog3Id: catalog3Id}).then(res => {
                if(res.data.list && res.data.list.length > 0){
                    if (this.formData.baseAttr && this.formData.baseAttr.length > 0) {
                        res.data.list.forEach(item => {
                            this.formData.baseAttr.filter(base => {
                                if(base.split(",")[0] == item.id){
                                    item.selectValue = base
                                }
                            })
                            
                        })
                    }
                    this.baseAttrList = res.data.list
                }
            })
        },
        getSaleAttrList(productId){
            this.getPmsProductSaleAttr(productId).then(res => {
                if (res.data && res.data.length > 0) {
                    if (this.formData.saleAttr && this.formData.saleAttr.length > 0) {
                        res.data.forEach(item => {
                            this.formData.saleAttr.filter(sale => {
                                if(sale.split(",")[0] == item.id){
                                    item.selectValue = sale
                                }
                            })
                        })
                    }
                    this.saleAttrList = res.data
                }
            })
        },
        handleSubmit() {
            this.$refs['formData'].validate((valid) => {
                if (valid) {
                    this.saveSkuInfo(this.formData).then(res => {
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
            this.formData.baseAttr = value
        },
        getSaleAttrValue(value){
            this.formData.saleAttr = value
        },
        validatePrice(rule, value, callback){
            if (value === '') {
                callback(new Error('商品价格不能为空'));
            } else {
                value = Number(value)
                if (typeof value === 'number' && !isNaN(value)) {
                    callback();
                } else {
                    callback(new Error('商品价格必须是数字'));
                }
            }
        }
    }
}
</script>