<template>
  <span @dblclick="startEdit" class="add-tag-outer">
    <span v-if="!isEditting">
      <Tag closable @on-close="onClose">{{attr.saleAttrValueName}}</Tag>
    </span>
    <span v-else class="add-tag-con">
      <Input v-model="edit" class="add-tag-input"/>
      <Button @click="saveEdit" style="padding: 6px 4px;" type="text"><Icon type="md-checkmark"></Icon></Button>
      <Button @click="cancelEdit" style="padding: 6px 4px;" type="text"><Icon type="md-close"></Icon></Button>
    </span>
  </span>
</template>
<script>
export default {
  name: 'addTag',
  props: {
    editCellingId: String,
    params: Object,
    attr: Object
  },
  computed: {
    isEditting () {
      return this.editCellingId === `editting-${this._uid}`
    }
  },
  data() {
    return {
      edit: '',
      edittingId: ''
    }
  },
  methods: {
    saveEdit () {
      this.attr.saleAttrValueName = this.edit
      this.$emit('on-save-edit')
      this.$emit('on-cancel-edit')
      this.edit = ''
    },
    cancelEdit () {
      this.$emit('on-cancel-edit')
      this.edit = ''
    },
    startEdit(){
      this.edit = this.attr.saleAttrValueName
      this.$emit('on-start-edit')
    },
    onClose(){
      this.$emit('on-close')
    }
  }
}
</script>

<style lang="less">
.add-tag-outer{
  display: inline-block;
  height: 100%;
  .add-tag-con{
    position: relative;
    height: 100%;
    .value-con{
      vertical-align: middle;
    }
    .add-tag-btn{
      position: absolute;
      right: 10px;
      top: 0;
      display: none;
    }
    &:hover{
      .add-tag-btn{
        display: inline-block;
      }
    }
  }
  .add-tag-con{
    .add-tag-input{
      width: ~"calc(100% - 50px)";
    }
  }
}
</style>
