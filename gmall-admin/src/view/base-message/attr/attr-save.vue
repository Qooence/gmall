<template>
    <Modal :title="title" v-model="value" @on-cancel="handlerCancel">
        <Form ref="formData" :model="formData" :label-width="80">
            <FormItem label="属性名称" :rules="{required: true, message:'属性名称不能为空', trigger: 'blur'}" prop='attrName'>
               <Input v-model="formData.attrName" placeholder="属性名称"></Input> 
            </FormItem>
            <FormItem>
                <Row>
                    <Col span="12">
                        <Button type="dashed" long @click="handleAdd" icon="md-add">增加属性值</Button>
                    </Col>
                </Row>
            </FormItem>
            <FormItem v-for="(item, index) in formData.attrValueList" :key="index" label="属性值" :prop="'attrValueList.' + index + '.valueName'"
                    :rules="{required: true, message:'属性值不能为空', trigger: 'blur'}">
                <Row>
                    <Col span="20">
                        <Input type="text" v-model="item.valueName" placeholder="属性值" clearable></Input>
                    </Col>
                    <Col span="2" offset="1">
                        <Button size="small" icon="md-close" @click="handleRemove(index)" type="primary" shape="circle"></Button>
                    </Col>
                </Row>
            </FormItem>
        </Form>
        <div slot="footer">
            <Button @click="handlerCancel">取消</Button>
            <Button type="primary" style="margin-left: 8px" @click="handleSubmit">确认</Button>
        </div>
    </Modal>
</template>

<script>
import { mapActions } from 'vuex'
export default {
    data() {
        return {
            value: false,
            title: '添加',
            formData: {
                attrValueList: []
            }
        }
    },
    methods: {
        ...mapActions(['saveAttrInfo','attrDetail']),
        init(id, catalog3Id) {
            this.formData.catalog3Id = catalog3Id;
            if(id){
                this.title = '修改'
                this.attrDetail(id).then(res =>{
                    this.formData.id = res.data.id
                    this.formData.catalog3Id = res.data.catalog3Id
                    this.$set(this.formData, 'attrName', res.data.attrName)
                    if(res.data.attrValueList){
                        let arr = res.data.attrValueList
                        for(let i = 0; i < arr.length; i++){
                            this.formData.attrValueList.push({
                                valueName: arr[i].valueName,
                                index: i
                            })
                        }
                    }
                })
            }else{
                this.title = '新增'
            }
            this.value = true
        },
        handleAdd() {
            this.index++
            this.formData.attrValueList.push({
                valueName: '',
                index: this.index
            })
        },
        handleRemove(index) {
            this.formData.attrValueList.splice(index,1);
        },
        handleSubmit() {
            this.$refs['formData'].validate((valid) => {
                if (valid) {
                    this.saveAttrInfo(this.formData).then(res => {
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
            this.formData = { attrValueList: []}
            this.$refs.formData.resetFields()
        }
    }
}
</script>