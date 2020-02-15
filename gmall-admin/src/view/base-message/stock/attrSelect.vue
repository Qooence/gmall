<script>
export default {
    data(){
        return {
            select: {

            }
        }
    },
    props: {
        attrList: {
            type: Array,
            default: () => []
        }
    },
    render() {
        let rows = []
        let i = 0
        let col = []
        this.attrList.forEach((item, index) => {
            let ops = [] 
            if (item.attrValueList && item.attrValueList.length > 0) {
                item.attrValueList.forEach(value => {
                    ops.push(<Option tag={value.attrId} value={value.id}>{value.valueName}</Option>)
                })
            }
            if (parseInt(index/3) === i) {
                col.push(
                    <Col span="8">
                        <FormItem label={item.attrName}>
                            <Select clearable value={this.select['select' + item.id]} on-input={(value) => {this.select['select' + item.id] = value}} placeholder={item.attrName} label-in-value on-on-change={obj => {
                                    if(obj){
                                        this.select['select' + item.id] = [obj.tag,obj.value,obj.label].join()
                                    }else{
                                        delete this.select['select' + item.id]
                                    }
                                    let arr = []
                                    for(let name in this.select){
                                        arr.push(this.select[name])
                                    }
                                    this.$emit('getValue',arr)
                                }
                            }>
                                {ops}
                            </Select>
                        </FormItem>
                    </Col>
                )
                if (index % 3 === 2) {
                rows.push(<Row>{col}</Row>)
                col.length = 0
                i++
                }
            }
        })
        rows.push(<Row>{col}</Row>) // 将剩下不满一行的加入
        return <div>{rows}</div>
    }
}
</script>