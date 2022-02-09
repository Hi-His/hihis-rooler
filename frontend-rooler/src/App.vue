<template>
  <div id="app">
    <div id="container"></div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import HelloWorld from './components/HelloWorld.vue'
import { Graph, CellView } from '@antv/x6'

import { GridLayout, CircularLayout } from '@antv/layout'
import { async } from '@antv/x6/lib/registry/marker/async'

@Component({
  components: {
    HelloWorld,
  },
})
export default class App extends Vue {
  nodes: any = {}
  graph: Graph
  gridLayout: GridLayout

  async init() {
    this.gridLayout = new GridLayout({
      type: 'grid',
      begin: [10, 10],
      width: 1000,
      height: 400,
      condense: false,
      preventOverlap: true,
      rows: 4,
      cols: 4,
    })

    this.graph = new Graph({
      container: document.getElementById('container'),
      width: 1920,
      height: 1080,
      grid: true,
      interacting: function (cellView: CellView) {
        return {
          nodeMovable: true,
          arrowheadMovable: true,
          vertexMovable: true,
          edgeMovable: true,
          vertexDeletable: true,
        }
      },
      connecting: {
        allowBlank: false,
        allowMulti: false,
        allowLoop: true,
        allowNode: true,
        allowEdge: true,
        allowPort: false,
      },
    })

    this.graph.on('edge:dblclick', async ({ e, x, y, edge, view }) => {
      const res = confirm('确定要删除指定连接吗')
      if (res) {
        const s = edge.getSourceCell().getData()
        const t = edge.getTargetCell().getData()

        let targetMap = s.domainMapping

        delete targetMap[t.domain]

        const res = await this.linkService(s, targetMap)

        if (res) {
          this.graph.removeEdge(edge.id)
        } else {
          console.log('删除失败')
          alert('删除失败')
        }
      }
    })

    this.graph.on('edge:connected', async ({ isNew, edge }) => {
      if (isNew) {
        const s = edge.getSourceCell().getData()
        const t = edge.getTargetCell().getData()

        let node = t.nodeName.split('@')[1]

        let targetMap = s.domainMapping || {}

        targetMap[t.domain] = node.split(':')[0]

        const res = await this.linkService(s, targetMap)

        if (!res) {
          console.log('节点未开启功能')
          alert('节点未开启功能')
          this.graph.removeEdge(edge.id)
        }
      }
    })
  }

  async getService() {
    this.nodes = {}
    const data: any = {
      nodes: [],
      edges: [],
    }
    var res = await this.axios.get('/getServices')
    const nodes = JSON.parse(JSON.stringify(res.data))

    // 渲染源节点
    for (const key in nodes) {
      if (Object.prototype.hasOwnProperty.call(nodes, key)) {
        const element = nodes[key]

        element.nodeName = key

        const ipPort = key.split('@')[1]

        const node = {
          id: element.nodeName,
          ip: element.ip,
          shape: 'rect',
          width: 64,
          height: 64,
          domain: element.domain,
          data: element,
          label: element.nodeName,
          domainMapping: element.domainMapping,
          ports: {
            groups: {
              in: {
                attrs: {
                  circle: {
                    r: 6,
                    magnet: true,
                    stroke: '#31d0c6',
                    strokeWidth: 2,
                    fill: '#fff',
                  },
                },
                position: 'top',
              },
              out: {
                attrs: {
                  circle: {
                    r: 6,
                    magnet: true,
                    stroke: '#31d0c6',
                    strokeWidth: 2,
                    fill: '#fff',
                  },
                },
                position: 'bottom',
              },
            },
            items: [
              {
                id: 'port1',
                group: 'in',
              },
            ],
          },
        }
        data.nodes.push(node)
        this.nodes[element.domain + ':' + ipPort.split(':')[0]] = node
      }
    }

    // 渲染边
    for (const key1 in this.nodes) {
      if (Object.prototype.hasOwnProperty.call(this.nodes, key1)) {
        const sourceNode = this.nodes[key1]
        const mapping = sourceNode.domainMapping

        // 若节点上存在映射关系
        if (mapping) {
          let needUpdate = false

          for (const key in mapping) {
            if (Object.prototype.hasOwnProperty.call(mapping, key)) {
              const targetNode = this.nodes[key + ':' + mapping[key]]
              // 如果目标节点存在

              if (targetNode) {
                data.edges.push({
                  source: {
                    cell: sourceNode.id,
                    port: 'port1',
                  },
                  target: { cell: targetNode.id, port: 'port1' },
                })
              } else {
                // 如果不存在，需要更新映射关系

                delete mapping[key]
                needUpdate = true
              }
            }
          }

          if (needUpdate) {
            this.linkService(sourceNode, mapping)
          }
        }
      }
    }

    const model = this.gridLayout.layout(data)

    this.graph.fromJSON(model)
  }

  async linkService(source, mapping) {
    var param: any = new Object()

    param.source = source

    param.target = mapping

    const res = await this.axios.post('/linkService', param)

    return res.data
  }

  mounted() {
    this.init()
    this.getService()

    setInterval(() => {
      this.getService()
    }, 10000)
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

.app {
  font-family: sans-serif;
  padding: 0;
  display: flex;
  padding: 16px 8px;
}

.app-content {
  flex: 1;
  height: 300px;
  margin-left: 8px;
  margin-right: 8px;
  box-shadow: 0 0 10px 1px #e9e9e9;
}
</style>
