<template>
  <div>
    <div v-if="changing">
      <Tag color="success">默认</Tag>
    </div>
    <div v-else>
       <Button type='primary' @click="setDefault">设为默认</Button>
    </div>
  </div>
</template>

<script>
export default {
    name: 'defaultButton',
    props: {
        changeDefault: String,
        params: Object
    },
    computed: {
        changing () {
            return this.changeDefault === `changing-${this.params.index}-${this.params.column.__id}`
        }
    },
    methods: {
        setDefault() {
            this.$emit('set-default')
            this.$emit('get-default', this.params.row)
        }
    }
}
</script>