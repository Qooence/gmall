<template>
  <div class="add-tag-outer">
    <div v-if="!isEditting" class="tables-edit-con">
      <Button @click="startAdd" class="add-tag-btn" style="padding: 2px 4px;" type="text">
        <Icon type="md-add"></Icon>
      </Button>
    </div>
    <div v-else class="add-tag-con">
      <Input v-model="tagName" class="add-tag-input"/>
      <Button @click="saveAdd" style="padding: 6px 4px;" type="text"><Icon type="md-checkmark"></Icon></Button>
      <Button @click="cancelAdd" style="padding: 6px 4px;" type="text"><Icon type="md-close"></Icon></Button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'addTag',
  props: {
    addCellingId: String,
    params: Object,
  },
  computed: {
    isEditting () {
      return this.addCellingId === `adding-${this.params.index}-${this.params.column.key}`
    }
  },
  data() {
    return {
      tagName: '' 
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
