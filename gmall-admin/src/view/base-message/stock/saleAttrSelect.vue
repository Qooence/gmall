<script>
export default {
    data(){
        return {
            select: {

            }
        }
    },
    props: {
        saleAttrList: {
            type: Array,
            default: () => []
        }
    },
    render() {
        let rows = []
        let i = 0
        let col = []
        this.saleAttrList.forEach((item, index) => {
            if(item.selectValue){
                this.select['select' + item.id] = item.selectValue
            }
            let ops = [] 
            if (item.saleAttrValues && item.saleAttrValues.length > 0) {
                item.saleAttrValues.forEach(value => {
                    ops.push(<Option value={value.saleAttrId + ',' + value.id}>{value.saleAttrValueName}</Option>)
                })
            }
            if (parseInt(index/3) === i) {
                col.push(
                    <Col span="8">
                        <FormItem label={item.saleAttrName}>
                            <Select clearable value={this.select['select' + item.id]}
                                label-in-value placeholder={item.saleAttrName} 
                                on-input={(value) => {this.select['select' + item.id] = value}}
                                on-on-change={obj => {
                                    let arr = []
                                    for(let name in this.select){
                                        if(this.select[name]){
                                            arr.push(this.select[name])
                                        }
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