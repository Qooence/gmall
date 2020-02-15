<template>
    <Modal :title="title" v-model="value" @on-cancel="handlerCancel" width="1024">
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
                <Upload multiple type="drag" action="//jsonplaceholder.typicode.com/posts/">
                    <div style="padding: 20px 0">
                        <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
                        <p>Click or drag files here to upload</p>
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
    </Modal>
</template>

<script>
import { mapActions } from 'vuex'
import addTag from './add-tag'
export default {
    components: {
        addTag
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
                        let components = []
                        if (row.saleAttrValues && row.saleAttrValues.length > 0) {
                            row.saleAttrValues.forEach(item => {
                                if(!item.saleAttrValueName) return
                                components.push(<Tag closable name={item.saleAttrValueName} on-on-close={(event, name) => {
                                        let newTag = row.saleAttrValues.filter((item) => (item.saleAttrValueName && item.saleAttrValueName !== name))
                                        this.$set(this.tableData[params.index], 'saleAttrValues', newTag)
                                    }
                                }
                                >{item.saleAttrValueName}</Tag>)
                            })
                        }
                        components.push(<addTag params={params} addCellingId={this.addingTagCellId} 
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
                            ></addTag>
                        )
                        return components
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
            }
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
                    this.tableData = res.data.productSaleAttrs
                })
            }else{
                this.title = '新增'
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
        }
    }
}
</script>