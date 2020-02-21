<template>
  <div class="add-tag-outer">
    <div class="tables-edit-con" style="display: inline-block;">
       <span v-for="(v, index) in attrValueList">
         <edit-tag :attr="v" :editCellingId="editCellingId" :params="params" :ref="'v'+index"
          @on-start-edit="startEdit($refs['v'+index])"
          @on-cancel-edit="cancelEdit"
          @on-close="onClose(index)"
          @on-save-edit="saveEdit(index,v)"
         ></edit-tag>
      </span>
    </div>
    <div v-if="!isAdding" class="tables-edit-con" style="display: inline-block;">
      <Button @click="startAdd" class="add-tag-btn" style="padding: 2px 4px;" type="text">
        <Icon type="md-add"></Icon>
      </Button>
    </div>
    <div v-else class="add-tag-con" style="display: inline-block;">
      <Input v-model="tagName" class="add-tag-input"/>
      <Button @click="saveAdd" style="padding: 6px 4px;" type="text"><Icon type="md-checkmark"></Icon></Button>
      <Button @click="cancelAdd" style="padding: 6px 4px;" type="text"><Icon type="md-close"></Icon></Button>
    </div>
  </div>
</template>

<script>
import editTag from './edit-tag'
export default {
  name: 'addTag',
  components: {
    editTag
  },
  props: {
    addCellingId: String,
    params: Object,
    attrValueList: Array
  },
  computed: {
    isAdding () {
      return this.addCellingId === `adding-${this.params.index}-${this.params.column.key}`
    }
  },
  data() {
    return {
      tagName: '',
      editCellingId: ''
    }
  },
  methods: {
    startAdd () {
      this.tagName = ''
      this.$emit('on-start-add')
    },
    saveAdd () {
      this.$emit('on-save-add', this.tagName)
      this.tagName = ''
    },
    cancelAdd () {
      this.$emit('on-cancel-add')
      this.tagName = ''
    },
    startEdit (_this) {
      this.editCellingId = `editting-${_this[0]._uid}`
    },
    cancelEdit () {
      this.editCellingId = ''
    },
    saveEdit (index,value) {
      this.$emit('on-save-edit',index,value)
    },
    onClose(index){
      this.$emit('on-close',index)
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
