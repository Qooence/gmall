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
            <Table border ref="imgTables" :columns="columns" :data="imgList" @on-selection-change="onChange"></Table>
        </Form>
        <div slot="footer">
            <Button @click="handlerCancel">取消</Button>
            <Button type="primary" style="margin-left: 8px" @click="handleSubmit">确认</Button>
        </div>
        <Modal title="查看图片" v-model="visible">
            <img :src="imgUrl" v-if="visible" style="width: 100%">
        </Modal>
    </Modal>
</template>

<script>
import { mapActions } from 'vuex'
import attrSelect from './attr-select'
import saleAttrSelect from './sale-attr-select'
import defaultButton from './default-button'
export default {
    components: {
        attrSelect,
        saleAttrSelect,
        defaultButton
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
                { title: '图片', align: 'center', align: 'center', tooltip: true,
                    render: (h, params) => {
                        if(params.row.imgUrl){
                            return [
                                h('div',
                                    {
                                        attrs:{
                                            class : 'demo-upload-list'
                                        }
                                    },
                                    [
                                        h('img',{
                                            attrs : {
                                                src : params.row.imgUrl,
                                            }
                                        }),
                                        h('div',{
                                                on : {
                                                    click : () => {
                                                        this.handleView(params.row.imgUrl);
                                                    }
                                                },
                                                attrs : {
                                                    class : 'demo-upload-list-cover'
                                                }
                                            },
                                            [
                                                h('Icon',{
                                                    props : {
                                                        type : 'ios-eye-outline'
                                                    }
                                                })
                                            ]
                                        )
                                    ]
                                )
                            ]
                        }
                    }
                },
                { title: '图片名称', align: 'center', key: 'imgName', tooltip: true },
                { title: '图片名称', align: 'center', key: 'id', tooltip: true },
                { title: '图片名称', align: 'center', key: 'productIm', tooltip: true },
                { type: 'selection', align: 'center'},
                { title: '操作', align: 'center',
                    render: (h, params) => {
                        // 回显默认按钮
                        let skuDefaultImg = this.formData.skuDefaultImg
                        if(skuDefaultImg && skuDefaultImg === params.row.imgUrl){
                            this.changeDefault = `changing-${params.index}-${params.column.__id}`
                        }
                        // 回显勾选
                        // if(this.updateReturnSkuImages && this.updateReturnSkuImages.length > 0){
                        //     let rows = []
                        //     this.updateReturnSkuImages.forEach(item => {
                        //         if(item.productImgId == params.row.id){
                        //             rows.push(params.row)
                        //             this.$refs.imgTables.objData[params.index]._isChecked = true
                        //         }
                        //     })
                        //     this.selectArry = rows
                        // }

                        return (<defaultButton params={params} changeDefault={this.changeDefault} 
                                on-set-default={() => {
                                    this.changeDefault = `changing-${params.index}-${params.column.__id}`
                                }}
                                on-get-default={(value) => {
                                    this.formData.skuDefaultImg = value.imgUrl
                                }}
                                ></defaultButton>
                        )
                    }
                }
            ],
            imgUrl: '',
            visible: false,
            selectArry: [],
            changeDefault: '',
            is: true,
            updateReturnSkuImages: [] // 更新时返回回来的图片列表 
        }
    },
    methods: {
        ...mapActions(['getAttrInfoList', 'getPmsProductSaleAttr','saveSkuInfo', 'skuDetail', 'productImageList']),
        init(id, { productId, productName, catalog3Id }) {
            this.formData.productName = productName
            this.formData.productId = productId
            this.formData.catalog3Id = catalog3Id
            this.getProductImageList(productId)
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
                    this.$set(this.formData, 'skuDefaultImg', res.data.skuDefaultImg)

                    // 回显勾选
                    if(res.data.skuImages && res.data.skuImages.length > 0){
                        if(this.imgList && this.imgList.length > 0) {
                            let rows = []
                            res.data.skuImages.forEach(item => {
                                this.imgList.forEach((img,index) => {
                                    if(item.productImgId == img.id){
                                        rows.push(img)
                                        this.$refs.imgTables.objData[index]._isChecked = true
                                    }
                                })
                            })
                            this.selectArry = rows
                        }
                        
                    }
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
        getProductImageList(productId){
            this.productImageList(productId).then(res => {
                if(res.data && res.data.length > 0){
                    this.imgList = res.data
                } 
            })
        },
        handleSubmit() {
            this.$refs['formData'].validate((valid) => {
                if (valid) {
                    if(this.selectArry.length > 0) {
                        this.selectArry.forEach(item => {
                            item.productImgId = item.id
                            if(item.imgUrl == this.formData.skuDefaultImg){
                                item.isDefault = 1
                            }
                            delete item.id
                        })
                        this.formData.skuImages = this.selectArry
                    }
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
            this.$refs.imgTables.selectAll(false)
            this.selectArry = []
            this.changeDefault = ''
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
        },
        handleView (imgUrl) {
            this.imgUrl = imgUrl
            this.visible = true
        },
        onChange (value) {
            this.selectArry = value
        }
    }
}
</script>
<style>
    .demo-upload-list{
        display: inline-block;
        width: 60px;
        height: 60px;
        text-align: center;
        line-height: 60px;
        border: 1px solid transparent;
        border-radius: 4px;
        overflow: hidden;
        background: #fff;
        position: relative;
        box-shadow: 0 1px 1px rgba(0,0,0,.2);
        margin-right: 4px;
    }
    .demo-upload-list img{
        width: 100%;
        height: 100%;
    }
    .demo-upload-list-cover{
        display: none;
        position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        background: rgba(0,0,0,.6);
    }
    .demo-upload-list:hover .demo-upload-list-cover{
        display: block;
    }
    .demo-upload-list-cover i{
        color: #fff;
        font-size: 20px;
        cursor: pointer;
        margin: 0 2px;
    }
</style>