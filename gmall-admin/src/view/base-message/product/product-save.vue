<template>
    <Modal :title="title" v-model="value" @on-cancel="handlerCancel" :width="width" :styles="{top: '50px'}">
        <Form ref="formData" :model="formData" :label-width="80">
            <Row>
                <Col span="8">
                    <FormItem label="一级标题">
                        <Select v-model="formData.catalog1Id" placeholder="一级标题" @on-change="catalog1Change" clearable>
                            <Option v-for="item in catalogList1" :value="item.id" :key="item.id">{{ item.name }}</Option>
                        </Select>
                    </FormItem>
                </Col>
                <Col span="8">
                    <FormItem label="二级标题">
                        <Select v-model="formData.catalog2Id" placeholder="二级标题" @on-change="catalog2Change" clearable>
                            <Option v-for="item in catalogList2" :value="item.id" :key="item.id">{{ item.name }}</Option>
                        </Select>
                    </FormItem>
                </Col>
                <Col span="8">
                    <FormItem label="三级标题">
                        <Select v-model="formData.catalog3Id" placeholder="三级标题" clearable>
                            <Option v-for="item in catalogList3" :value="item.id" :key="item.id">{{ item.name }}</Option>
                        </Select>
                    </FormItem>
                </Col>
            </Row>
            <FormItem label="系列名称" :rules="{required: true, message:'商品名称不能为空', trigger: 'blur'}" prop='productName'>
               <Input v-model="formData.productName" placeholder="商品名称"></Input> 
            </FormItem>
            <FormItem label="系列描述" :rules="{required: true, message:'商品描述不能为空', trigger: 'blur'}" prop='description'>
               <Input type="textarea" :rows="4" v-model="formData.description" placeholder="商品描述"></Input> 
            </FormItem>
            <FormItem label="图片上传">
                <div class="demo-upload-list" v-for="item in uploadList">
                    <template v-if="item.status === 'finished'">
                        <img :src="item.url">
                        <div class="demo-upload-list-cover">
                            <Icon type="ios-eye-outline" @click.native="handleView(item.url)"></Icon>
                            <Icon type="ios-trash-outline" @click.native="handleRemove(item)"></Icon>
                        </div>
                    </template>
                    <template v-else>
                        <Progress v-if="item.showProgress" :percent="item.percentage" hide-info></Progress>
                    </template>
                </div>
                <Upload
                    ref="upload"
                    :show-upload-list="false"
                    :default-file-list="defaultList"
                    :on-success="handleSuccess"
                    :format="['jpg','jpeg','png']"
                    :max-size="2048"
                    :on-format-error="handleFormatError"
                    :on-exceeded-size="handleMaxSize"
                    multiple
                    type="drag"
                    action="//127.0.0.1:8081/product/fileUpload"
                    style="display: inline-block;width:58px;">
                    <div style="width: 58px;height:58px;line-height: 58px;">
                        <Icon type="ios-camera" size="20"></Icon>
                    </div>
                </Upload>
            </FormItem>
            <Row>
                <Col span="4">
                    <Button type="primary" long @click="handleAdd" icon="md-add">增加销售属性</Button>
                </Col>
            </Row>
            <Table ref="tables" :columns="columns" :data="tableData" height="300"></Table>
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
import addTag from './add-tag'
export default {
    components: {
        addTag
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
            catalogList1: [],
            catalogList2: [],
            catalogList3: [],
            saleAttrOption: [],
            addingTagCellId: '',
            selectAttr: [],
            columns: [
                {title: '序号', width:80, type: 'index', align: 'center'},
                {title: '销售属性名称', align: 'center',
                    render: (h, params) => {
                        return (<Select value={this.tableData[params.index].saleAttrId} style="width:100px" label-in-value on-on-change={(obj) => {
                                this.tableData[params.index].saleAttrId = obj.value
                                this.tableData[params.index].saleAttrName = obj.label
                            }
                        }>{this.saleAttrOption}</Select>);
                        

                    }
                },
                { title: '销售属性值', width: 500, align: 'center',
                    render: (h, params) => {
                        let row = params.row
                        return <addTag params={params} attrValueList={row.saleAttrValues ? row.saleAttrValues : []} addCellingId={this.addingTagCellId} 
                            on-on-start-add={() => {this.addingTagCellId = `adding-${params.index}-${params.column.key}`}} 
                            on-on-save-add={(value)=>{
                                    this.tableData[params.index].saleAttrValues.push(
                                        {
                                            saleAttrId: '', 
                                            saleAttrValueName: value
                                        }
                                    )
                                    this.addingTagCellId = ''
                                }
                            }
                            on-on-cancel-add={() => {this.addingTagCellId = ''}}
                            on-on-close={(index) => {
                                this.tableData[params.index].saleAttrValues.splice(index,1)
                            }}
                            on-on-save-edit={(index,value) => {
                                this.tableData[params.index].saleAttrValues[index] = value
                            }}
                            ></addTag>
                    }
                },
                {
                    title: '操作',
                    align: 'center',
                    render: (h, params, vm) => {
                        return h('Poptip', {
                            props: {
                                confirm: true,
                                title: '你确定要删除吗?'
                            },
                            on: {
                                'on-ok': () => {
                                    let newData = this.tableData.filter((item, index) => index !== params.index)
                                    this.$set(this, 'tableData', newData)
                                }
                            }
                        },[h('Button', {props: {type: 'error'}}, '删除')])
                    }
                }
            ],
            tableData: [],
            formData: {
               productName: '',
               description: ''
            },
            defaultList: [],
            imgUrl: '',
            visible: false,
            uploadList: []
        }
    },
    methods: {
        ...mapActions(['getCatalog1','getCatalog2','getCatalog3', 'getSaleAttr', 'saveProductInfo','productDetail']),
        init(id, {catalog1Id, catalog2Id, catalog3Id}) {
            this.formData.catalog1Id = catalog1Id
            this.formData.catalog2Id = catalog2Id
            this.formData.catalog3Id = catalog3Id
            this.getCatalog1List()
            this.getCatalog2List(catalog1Id)
            this.getCatalog3List(catalog2Id)
            if(id){
                this.title = '修改'
                this.productDetail(id).then(res =>{
                    this.formData.id = res.data.id
                    this.formData.productName = res.data.productName
                    this.formData.description = res.data.description
                    this.tableData = res.data.productSaleAttrs ? res.data.productSaleAttrs : [] 
                    if(res.data.productImages && res.data.productImages.length > 0){
                        res.data.productImages.forEach(item => {
                            this.uploadList.push({
                                name: item.imgName,
                                url: item.imgUrl,
                                status: 'finished' 
                            })
                        })
                    }
                    this.$refs.upload.fileList = this.uploadList 
                })
            }else{
                this.title = '新增'
                this.uploadList = this.$refs.upload.fileList
            }
            this.value = true
            this.getSaleAttrList()
        },
        handleAdd() {
            this.tableData.push({
                saleAttrValues: []
            })
        },
        handleSubmit() {
            this.$refs['formData'].validate((valid) => {
                if (valid) {
                    this.formData.productSaleAttrs = this.tableData
                    if(this.uploadList && this.uploadList.length > 0){
                        this.uploadList.forEach(item => {
                            item.imgName = item.name
                            item.imgUrl = item.url
                        })
                        this.formData.productImages = this.uploadList
                    }
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
            this.formData = {
                productName: '',
                description: ''
            }
            this.saleAttrOption = []
            this.tableData = []
            this.$refs.upload.fileList = []
            this.uploadList = []
            this.$refs.formData.resetFields()
        },
        catalog1Change(value){
            this.$set(this.formData, 'catalog3Id', '')
            this.catalogList3.length = 0
            if(value){
                this.getCatalog2List(value)
            }else{
                this.$set(this.formData, 'catalog2Id', '')
                this.catalogList2.length = 0
            }
        },
        catalog2Change(value){
            if(value){
                this.getCatalog3List(value);
            }else{
                this.$set(this.formData, 'catalog3Id', '')
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
        getSaleAttrList(){
            this.getSaleAttr().then(res => {
                if(res && res.data){
                    res.data.forEach(item => {
                        this.saleAttrOption.push(
                            (<Option value={item.id}>{item.name}</Option>)
                        )
                    })
                }
            })
        },
        handleView (imgUrl) {
            this.imgUrl = imgUrl
            this.visible = true
        },
        handleRemove (file) {
            const fileList = this.$refs.upload.fileList;
            this.$refs.upload.fileList.splice(fileList.indexOf(file), 1)
        },
        handleSuccess (res, file) {
            if(res.data){
                file.url = res.data
                file.name = res.data.split('/').pop()
            }
        },
        handleFormatError (file) {
            this.$Notice.warning({
                title: 'The file format is incorrect',
                desc: 'File format of ' + file.name + ' is incorrect, please select jpg or png.'
            })
        },
        handleMaxSize (file) {
            this.$Notice.warning({
                title: 'Exceeding file size limit',
                desc: 'File  ' + file.name + ' is too large, no more than 2M.'
            })
        }
    }
}
</script>
<style scoped>
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