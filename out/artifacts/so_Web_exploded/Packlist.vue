<template>
  <el-row>
    <el-col style="padding:10px 0px;">
      <el-col style="padding-left: 10px">
        <el-button size="mini" type="success" icon="el-icon-plus" @click="add_Buspack()">添加</el-button>
        <el-button type="primary" size="mini" icon="el-icon-check" @click="saveBuspacklist()">
          保存
        </el-button>
      </el-col>
    </el-col>
    <el-col>
      <el-form style="height: 500px;overflow-y: auto;">
        <el-form-item style="margin-bottom: unset;" v-for="(buspack, index) in buspacklist" :key="index"
                      :class="index % 2 == 0 ? 'ufms-black':'ufms-white'">
          <el-col style="padding: 8px 10px;border-bottom: 1px solid #e5e5e5;">
            <el-button type="danger" size="mini"
                       icon="el-icon-delete" @click.prevent="remove_Buspack(buspacklist,buspack)">删除
            </el-button>
            <el-button type="primary" size="mini"
                       icon="el-icon-download" @click="add_Buspack()">增加(PL)
            </el-button>
            <el-button type="primary" size="mini"
                       icon="el-icon-download" @click="add_buscommercecargomixed(buspacklist,buspack)">增加(SubPL)
            </el-button>
          </el-col>
          <el-col :span="5" style="padding:0px 10px;">
            <el-col>
              <div class="title-font">产品名/产品英文名</div>
              <el-col :span="11">
                <el-input v-model="buspack.factoryname" size="mini" placeholder="产品名"></el-input>
              </el-col>
              <el-col :span="1">
                <div class="symbol-css">/</div>
              </el-col>
              <el-col :span="12">
                <el-input v-model="buspack.factorynamee" size="mini" placeholder="产品英文名"></el-input>
              </el-col>
            </el-col>
            <el-col>
              <div class="title-font">唛头/SKU</div>
              <el-col :span="11">
                <el-input v-model="buspack.markno" size="mini" placeholder="唛头"></el-input>
              </el-col>
              <el-col :span="1">
                <div class="symbol-css">/</div>
              </el-col>
              <el-col :span="12">
                <el-input v-model="buspack.sku" size="mini" placeholder="SKU"></el-input>
              </el-col>
            </el-col>
            <el-col>
              <div class="title-font">Reference ID/Shipment ID</div>
              <el-col :span="11">
                <el-input v-model="buspack.referenceid" size="mini" placeholder="Reference ID"></el-input>
              </el-col>
              <el-col :span="1">
                <div class="symbol-css">/</div>
              </el-col>
              <el-col :span="12">
                <el-input v-model="buspack.shipmentid" size="mini" placeholder="Shipment ID"></el-input>
              </el-col>
            </el-col>
            <el-col v-if="buspack.isShowSubPl">
              <div class="title-font">备注</div>
              <el-input type="textarea" :rows="2" v-model="buspack.remark" placeholder="备注"></el-input>
            </el-col>
          </el-col>
          <el-col :span="7" style="padding:0px 10px;">
            <el-col>
              <div class="title-font">包装件数/托盘数/柜型</div>
              <el-col :span="5">
                <el-input type="number" v-model="buspack.piece" placeholder="件数" size="mini" @input="changePackPiece(buspack,index)"></el-input>
              </el-col>
              <el-col :span="1">
                <div class="symbol-css">|</div>
              </el-col>
              <el-col :span="6">
                <el-select clearable v-model="buspack.packagee" filterable size="mini" placeholder="包装类型">
                  <el-option v-for="(item,index) in packagelist" :key="index" :label="item.namee" :value="item.namee">
                    <div class="ufms-underline">
                      <span>{{ item.namee }}</span>
                    </div>
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="1">
                <div class="symbol-css">/</div>
              </el-col>
              <el-col :span="5">
                <el-input type="number" v-model="buspack.piece2" placeholder="托盘数" size="mini"></el-input>
              </el-col>
              <el-col :span="1">
                <div class="symbol-css">/</div>
              </el-col>
              <el-col :span="5">
                <el-select clearable v-model="buspack.cntype" filterable size="mini" placeholder="柜型">
                  <el-option v-for="(item,index) in cntypeList" :key="index" :label="item.code" :value="item.code">
                    <div class="ufms-underline">
                      <span>{{ item.code }}</span>
                    </div>
                  </el-option>
                </el-select>
              </el-col>
            </el-col>
            <el-col>
              <el-col :span="18">
                <div class="title-font">毛重 ({{ buspack.grswgt }} x {{ buspack.piece }}PKGS = {{ buspack.grswgttotal }}KGS)</div>
                <el-col :span="11">
                  <el-input type="number" v-model="buspack.grswgt" placeholder="单件毛重" size="mini" @input="changePackGrswgt(buspack,index)"></el-input>
                </el-col>
                <el-col :span="1">
                  <div class="symbol-css">/</div>
                </el-col>
                <el-col :span="12">
                  <el-input type="number" v-model="buspack.grswgttotal" placeholder="合计毛重(KGS)" size="mini"></el-input>
                </el-col>
              </el-col>
              <el-col :span="6">
                <div class="title-font">单件净重</div>
                <el-input type="number" v-model="buspack.netweight" placeholder="单件净重" size="mini"></el-input>
              </el-col>
            </el-col>
            <el-col>
              <div class="title-font">规格(长X宽X高) = 体积</div>
              <el-col :span="4">
                <el-input type="number" v-model="buspack.cbmlength" placeholder="厘米" size="mini" @input="changePackLength(buspack,index)"></el-input>
              </el-col>
              <el-col :span="1">
                <div class="symbol-css">X</div>
              </el-col>
              <el-col :span="4">
                <el-input type="number" v-model="buspack.cbmwidth" placeholder="厘米" size="mini" @input="changePackWidth(buspack,index)"></el-input>
              </el-col>
              <el-col :span="1">
                <div class="symbol-css">X</div>
              </el-col>
              <el-col :span="4">
                <el-input type="number" v-model="buspack.cbmheight" placeholder="厘米" size="mini" @input="changePackHeight(buspack,index)"></el-input>
              </el-col>
              <el-col :span="1">
                <div class="symbol-css">=</div>
              </el-col>
              <el-col :span="4">
                <el-input type="number" v-model="buspack.cbm" placeholder="单件体积(CBM)" size="mini"></el-input>
              </el-col>
              <el-col :span="1">
                <div class="symbol-css">=</div>
              </el-col>
              <el-col :span="4">
                <el-input type="number" v-model="buspack.cbm2" placeholder="合计(CBM)" size="mini"></el-input>
              </el-col>
            </el-col>
            <el-col>
              <div class="title-font">产品链接(URL)</div>
              <el-col :span="18">
                <el-input v-model="buspack.producturl" size="mini"></el-input>
              </el-col>
            </el-col>
          </el-col>
          <el-col :span="12" v-if="!buspack.isShowSubPl">
            <el-col :span="8" style="padding:0px 10px;">
              <el-col>
                <div class="title-font">单价 X 商品数量 = 总价(USD)</div>
                <el-col :span="7">
                  <el-input type="number" v-model="buspack.price" placeholder="单价" size="mini" @input="changePackPrice(buspack,index)"></el-input>
                </el-col>
                <el-col :span="1">
                  <div class="symbol-css">X</div>
                </el-col>
                <el-col :span="7">
                  <el-input type="number" v-model="buspack.piece3" placeholder="商品数量" size="mini" @input="changePackPrice3(buspack,index)"></el-input>
                </el-col>
                <el-col :span="1">
                  <div class="symbol-css">=</div>
                </el-col>
                <el-col :span="8">
                  <el-input type="number" v-model="buspack.priceall" placeholder="合计(USD)" size="mini"></el-input>
                </el-col>
              </el-col>
              <el-col>
                <div class="title-font">商品编码/进口国</div>
                <el-col :span="13">
                  <el-input v-model="buspack.outerpackagecode" size="mini"></el-input>
                </el-col>
                <el-col :span="1">
                  <div class="symbol-css">/</div>
                </el-col>
                <el-col :span="10">
                  <el-input v-model="buspack.importingcountry" size="mini"></el-input>
                </el-col>
              </el-col>
              <el-col>
                <div class="title-font">关税/率</div>
                <el-col :span="8">
                  <el-input type="number" v-model="buspack.taxrate" size="mini"></el-input>
                </el-col>
                <el-col :span="1">
                  <div class="symbol-css">=</div>
                </el-col>
                <el-col :span="15">
                  <el-input v-model="buspack.dutyamt" size="mini">
                    <el-button slot="append" type="primary" size="mini">
                      关税计算
                    </el-button>
                  </el-input>
                </el-col>
              </el-col>
            </el-col>
            <el-col :span="8" style="padding:0px 10px;">
              <el-col>
                <div class="title-font">材质(中文/英文)</div>
                <el-col :span="11">
                  <el-input v-model="buspack.materialnamec" size="mini"></el-input>
                </el-col>
                <el-col :span="1">
                  <div class="symbol-css">/</div>
                </el-col>
                <el-col :span="12">
                  <el-input v-model="buspack.materialnamee" size="mini"></el-input>
                </el-col>
              </el-col>
              <el-col>
                <div class="title-font">用途(中文/英文)</div>
                <el-col :span="11">
                  <el-input v-model="buspack.usagenamec" size="mini"></el-input>
                </el-col>
                <el-col :span="1">
                  <div class="symbol-css">/</div>
                </el-col>
                <el-col :span="12">
                  <el-input v-model="buspack.usagenamee" size="mini"></el-input>
                </el-col>
              </el-col>
              <el-col>
                <div class="title-font">品牌(中文/英文)</div>
                <el-col :span="11">
                  <el-input v-model="buspack.brandnamec" size="mini"></el-input>
                </el-col>
                <el-col :span="1">
                  <div class="symbol-css">/</div>
                </el-col>
                <el-col :span="12">
                  <el-input v-model="buspack.brandnamee" size="mini"></el-input>
                </el-col>
              </el-col>
              <el-col>
                <div class="title-font">型号(中文/英文)</div>
                <el-col :span="11">
                  <el-input v-model="buspack.modelnamec" size="mini"></el-input>
                </el-col>
                <el-col :span="1">
                  <div class="symbol-css">/</div>
                </el-col>
                <el-col :span="12">
                  <el-input v-model="buspack.modelnamee" size="mini"></el-input>
                </el-col>
              </el-col>
            </el-col>
            <el-col :span="8" style="padding:0px 10px;">
              <el-col>
                <div class="title-font">备注</div>
                <el-input type="textarea" :rows="2" v-model="buspack.remark">
                </el-input>
              </el-col>
              <el-col style="text-align: end;padding-top: 10px;">
                <el-upload
                    action="#"
                    :before-upload="beforeUpload.bind('', {'linkid':buspack.id})"
                    :on-preview="handlePreview"
                    :on-remove="handleRemove"
                    :on-change="handleChange"
                    :file-list="buspack.filelist"
                    list-type="text"
                    :http-request="httpRequest.bind('', {'linkid':buspack.id})">
                  <el-button size="mini" type="primary">产品图片</el-button>
                </el-upload>
              </el-col>
            </el-col>
          </el-col>
          <!--混装部分-->
          <el-col style="background-color: #ebeef5;" :span="12" v-if="buspack.isShowSubPl">
            <el-form>
              <el-form-item style="margin-bottom: unset;"
                            v-for="(buscommercecargomixed, mixedIndex) in buspack.buscommercecargomixedlist" :key="mixedIndex">
                <el-col style="padding: 10px;text-align: end;">
                  <el-button type="primary" size="mini" icon="el-icon-plus" @click="add_buscommercecargomixed(buspacklist,buspack)">
                    增加
                  </el-button>
                  <el-button type="danger" size="mini" icon="el-icon-delete"
                             @click="remove_buscommercecargomixed(buspacklist,buspack,buspack.buscommercecargomixedlist,buscommercecargomixed)">
                    删除
                  </el-button>
                </el-col>
                <el-col :span="8" style="padding:0px 10px;">
                  <el-col>
                    <div class="title-font">单价 X 商品数量 = 总价(USD)</div>
                    <el-col :span="7">
                      <el-input type="number" v-model="buscommercecargomixed.unitprice" size="mini"
                                @input="changeMixedUnit(buscommercecargomixed,index,mixedIndex)"></el-input>
                    </el-col>
                    <el-col :span="1">
                      <div class="symbol-css">/</div>
                    </el-col>
                    <el-col :span="7">
                      <el-input type="number" v-model="buscommercecargomixed.pieces" size="mini"
                                @input="changeMixedPieces(buscommercecargomixed,index,mixedIndex)"></el-input>
                    </el-col>
                    <el-col :span="1">
                      <div class="symbol-css">/</div>
                    </el-col>
                    <el-col :span="8">
                      <el-input type="number" v-model="buscommercecargomixed.totalprice" size="mini"></el-input>
                    </el-col>
                  </el-col>
                  <el-col>
                    <div class="title-font">商品编码/进口国</div>
                    <el-col :span="13">
                      <el-input v-model="buscommercecargomixed.hscode" size="mini"></el-input>
                    </el-col>
                    <el-col :span="1">
                      <div class="symbol-css">/</div>
                    </el-col>
                    <el-col :span="10">
                      <el-input v-model="buscommercecargomixed.importcountrycode" size="mini"></el-input>
                    </el-col>
                  </el-col>
                  <el-col>
                    <div class="title-font">关税/率</div>
                    <el-col :span="8">
                      <el-input v-model="buscommercecargomixed.dutyrate" size="mini"></el-input>
                    </el-col>
                    <el-col :span="1">
                      <div class="symbol-css">=</div>
                    </el-col>
                    <el-col :span="15">
                      <el-input v-model="buscommercecargomixed.dutyamt" size="mini">
                        <el-button slot="append" type="primary" size="mini">
                          关税计算
                        </el-button>
                      </el-input>
                    </el-col>
                  </el-col>
                </el-col>
                <el-col :span="8" style="padding:0px 10px;">
                  <el-col>
                    <div class="title-font">毛重/体积</div>
                    <el-col :span="11">
                      <el-input type="number" v-model="buscommercecargomixed.gwttl" size="mini"></el-input>
                    </el-col>
                    <el-col :span="1">
                      <div class="symbol-css">/</div>
                    </el-col>
                    <el-col :span="12">
                      <el-input type="number" v-model="buscommercecargomixed.volttl" size="mini"></el-input>
                    </el-col>
                  </el-col>
                  <el-col>
                    <div class="title-font">材质/用途</div>
                    <el-col :span="11">
                      <el-input v-model="buscommercecargomixed.material" size="mini"></el-input>
                    </el-col>
                    <el-col :span="1">
                      <div class="symbol-css">/</div>
                    </el-col>
                    <el-col :span="12">
                      <el-input v-model="buscommercecargomixed.useage" size="mini"></el-input>
                    </el-col>
                  </el-col>
                  <el-col>
                    <div class="title-font">品牌/型号</div>
                    <el-col :span="11">
                      <el-input v-model="buscommercecargomixed.brand" size="mini"></el-input>
                    </el-col>
                    <el-col :span="1">
                      <div class="symbol-css">/</div>
                    </el-col>
                    <el-col :span="12">
                      <el-input v-model="buscommercecargomixed.model" size="mini"></el-input>
                    </el-col>
                  </el-col>
                </el-col>
                <el-col :span="8" style="padding:0px 10px;">
                  <el-col>
                  </el-col>
                  <el-col>
                    <div class="title-font">产品名/产品英文名</div>
                    <el-col :span="11">
                      <el-input v-model="buscommercecargomixed.cargoname" size="mini"></el-input>
                    </el-col>
                    <el-col :span="1">
                      <div class="symbol-css">/</div>
                    </el-col>
                    <el-col :span="12">
                      <el-input v-model="buscommercecargomixed.cargonameen" size="mini"></el-input>
                    </el-col>
                  </el-col>
                  <el-col style="text-align: end;padding-top: 10px;">
                    <el-upload
                        action="#"
                        :before-upload="beforeUpload.bind('', {'linkid':buscommercecargomixed.id})"
                        :on-preview="handlePreview"
                        :on-remove="handleRemove"
                        :on-change="handleChange"
                        :file-list="buscommercecargomixed.filelist"
                        list-type="text"
                        :http-request="httpRequest.bind('', {'linkid':buscommercecargomixed.id,'index':mixedIndex,
                        'buscommercecargomixed':buscommercecargomixed,'buspack':buspack})">
                      <el-button size="mini" type="primary">产品图片</el-button>
                    </el-upload>
                  </el-col>
                </el-col>
              </el-form-item>
            </el-form>
          </el-col>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<script src="admin/js/config.js"></script>

<script>
module.exports = {
  props: {
    finajobs: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      serviceList: [],
      companylist: [],
      deliveryList: [],
      cntypeList: [],
      goodtypelist: [],
      packagelist: [],
      serverTypeList: [],
      servicetermList: [],
      deliveryStatusList: [],
      buspacklist: [
        {
          buscommercecargomixedlist: [{
            totalprice: ''
          }],
        }
      ],
      buspackold: {
        id: '',
        linkid: '',
        linktbl: '',
        factoryname: '',
        factorynamee: '',
        markno: '',
        sku: '',
        referenceid: '',
        shipmentid: '',
        piece: '',
        packagee: '',
        piece2: '',
        cntype: '',
        grswgt: '',
        grswgttotal: '',
        netweight: '',
        cbmlength: '',
        cbmwidth: '',
        cbmheight: '',
        cbm2: '',
        producturl: '',
        price: '',
        piece3: '',
        priceall: '',
        outerpackagecode: '',
        importingcountry: '',
        taxrate: '',
        dutyamt: '',
        materialnamec: '',
        materialnamee: '',
        usagenamec: '',
        usagenamee: '',
        brandnamec: '',
        brandnamee: '',
        modelnamec: '',
        modelnamee: '',
        remark: '',
        buscommercecargomixedlist: [],
        isShowSubPl: false,
        filelist: []
      },
      buscommercecargomixedold: {
        id: '',
        ctnsid: '',
        cargoname: '',
        cargonameen: '',
        unitprice: '',
        pieces: '',
        totalprice: '',
        hscode: '',
        dutyrate: '',
        dutyamt: '',
        gwttl: '',
        volttl: '',
        material: '',
        useage: '',
        brand: '',
        model: '',
        importcountrycode: '',
        filelist: []
      }
    }
  },
  methods: {
    //新增装箱
    add_Buspack() {
      this.buspacklist.push(Object.assign({}, this.buspackold));
    },
    //删除装箱
    remove_Buspack(thisbuspacklist, thisbuspack) {
      thisbuspacklist.splice(thisbuspacklist.indexOf(thisbuspack), 1);
    },
    //新增混装箱
    add_buscommercecargomixed(thisbuspacklist, thisbuspack) {
      thisbuspack.buscommercecargomixedlist.push(Object.assign({}, this.buscommercecargomixedold));
      thisbuspack.isShowSubPl = true;
    },
    //删除混装箱
    remove_buscommercecargomixed(thisbuspacklist, thisbuspack, thisbuscommercecargomixedlist, thisbuscommercecargomixed) {
      thisbuscommercecargomixedlist.splice(thisbuscommercecargomixedlist.indexOf(thisbuscommercecargomixed), 1);
      if (thisbuscommercecargomixedlist.length == 0) {
        thisbuspack.isShowSubPl = false;
      }
    },
    beforeUpload(parame, file) {
      if (parame.linkid == '') {
        this.$message.error('请保存后再上传文件!');
        return false;
      }
      const fileSuffix = file.name.substring(file.name.lastIndexOf(".") + 1);
      const whiteList = ["jpg", "png", "jpeg", "bmp", "tiff"];
      if (whiteList.indexOf(fileSuffix) === -1) {
        this.$message.error(' 请选择图片(.jpg .png .jpeg .bmp .tiff)!');
        return false;
      }
    },
    handlePreview(file) {
      console.log(file);
    },
    handleRemove(file, fileList) {
      console.log(file);
    },
    // 上传文件
    handleChange(file, fileList) {
      console.log(file);
    },
    //上传文件
    httpRequest(parame, item) {
      let thisfileList = this.fileList;
      let fd = new FormData();
      fd.append('filename', item.file);
      fd.append('linkid', parame.linkid);
      this.$http.post(
          'http://cloud.gsitsystem.com/scp/buspacklist?method=uploadsomefile'
          , fd
          , {headers: {'Content-Type': 'multipart/form-data'}}
      ).then(response => {
        this.$message.success('上传成功');
      })
    },
    //获取集装箱列表数据
    queryBuspacklist() {
      if (this.finajobs.id != '' && this.finajobs.id != null) {
        var url = "http://cloud.gsitsystem.com/scp/buspacklist?method=query";
        this.$http.post(url, this.finajobs).then(function (res) {
          this.buspacklist = [];
          var thisbuspacklistArray = res.body.buspacklist;
          if (thisbuspacklistArray == undefined) {
            this.buspacklist.push(Object.assign({}, this.buspackold));
          } else {
            for (let i = 0; i < thisbuspacklistArray.length; i++) {
              let thisbuspack = eval('(' + thisbuspacklistArray[i].buspacklist.value + ')');
              thisbuspack.buscommercecargomixedlist = JSON.parse(thisbuspack.buscommercecargomixedlist) == null
                  ? [] : JSON.parse(thisbuspack.buscommercecargomixedlist);
              thisbuspack.isShowSubPl = thisbuspack.buscommercecargomixedlist.length == 0 ? false : true;
              thisbuspack.filelist = JSON.parse(thisbuspack.filelist) == null ? [] : JSON.parse(thisbuspack.filelist);
              for (let j = 0; j < thisbuspack.buscommercecargomixedlist.length; j++) {
                thisbuspack.buscommercecargomixedlist[j].filelist = JSON.parse(thisbuspack.buscommercecargomixedlist[j].filelist) == null
                    ? [] : JSON.parse(thisbuspack.buscommercecargomixedlist[j].filelist);
              }
              this.buspacklist.push(thisbuspack);
            }
          }
        });
      }
    },
    //获取基本参数
    selectCommon() {
      var url = "http://cloud.gsitsystem.com/scp/api?method=common";
      this.$http.post(url, {query: '', methodFlag: "common"}).then(function (res) {
        if (res.body != '') {
          //包装类型
          this.packagelist = eval('(' + res.body.package + ')');
          //柜型
          this.cntypeList = eval('(' + res.body.cntype + ')');
        }
      });
    },
    //保存
    saveBuspacklist() {
      var url = "http://cloud.gsitsystem.com/scp/buspacklist?method=save";
      this.$http.post(url, {"finajobs": this.finajobs, "buspacklist": this.buspacklist}).then(function (res) {
        if (res.body.data != null) {
          this.$message.success("保存完成");
          this.queryBuspacklist();
        } else {
          this.$message.error(res.body.tip);
        }
      });
    },
    //监听包装件数值
    changePackPiece(val, index) {
      if ((val.piece != undefined && val.piece != '') && (val.grswgt != undefined && val.grswgt != '')) {
        this.buspacklist[index].grswgttotal = val.piece * val.grswgt;
      }
    },
    //监听毛重
    changePackGrswgt(val, index) {
      if ((val.piece != undefined && val.piece != '') && (val.grswgt != undefined && val.grswgt != '')) {
        this.buspacklist[index].grswgttotal = val.piece * val.grswgt;
      }
    },
    //监听单价
    changePackPrice(val, index) {
      if ((val.price != undefined && val.price != '') && (val.piece3 != undefined && val.piece3 != '')) {
        this.buspacklist[index].priceall = val.price * val.piece3;
      }
    },
    //监听商品数量
    changePackPrice3(val, index) {
      if ((val.price != undefined && val.price != '') && (val.piece3 != undefined && val.piece3 != '')) {
        this.buspacklist[index].priceall = val.price * val.piece3;
      }
    },
    //监听长度录入
    changePackLength(val, index) {
      if ((val.cbmlength != undefined && val.cbmlength != '')
          && (val.cbmwidth != undefined && val.cbmwidth != '')
          && (val.cbmheight != undefined && val.cbmheight != '')) {
        this.buspacklist[index].cbm = Number((val.cbmlength * val.cbmwidth * val.cbmheight) / 1000000).toFixed(3);
        this.buspacklist[index].cbm2 = Number(this.buspacklist[index].cbm * this.buspacklist[index].piece).toFixed(3);
      }
    },
    //监听宽度录入
    changePackWidth(val, index) {
      if ((val.cbmlength != undefined && val.cbmlength != '')
          && (val.cbmwidth != undefined && val.cbmwidth != '')
          && (val.cbmheight != undefined && val.cbmheight != '')) {
        this.buspacklist[index].cbm = Number((val.cbmlength * val.cbmwidth * val.cbmheight) / 1000000).toFixed(3);
        this.buspacklist[index].cbm2 = Number(this.buspacklist[index].cbm * this.buspacklist[index].piece).toFixed(3);
      }
    },
    //监听高度录入
    changePackHeight(val, index) {
      if ((val.cbmlength != undefined && val.cbmlength != '')
          && (val.cbmwidth != undefined && val.cbmwidth != '')
          && (val.cbmheight != undefined && val.cbmheight != '')) {
        this.buspacklist[index].cbm = Number((val.cbmlength * val.cbmwidth * val.cbmheight) / 1000000).toFixed(3);
        this.buspacklist[index].cbm2 = Number(this.buspacklist[index].cbm * this.buspacklist[index].piece).toFixed(3);
      }
    },
    //监听混装单价
    changeMixedUnit(val, index, mixIndex) {
      if ((val.unitprice != undefined && val.unitprice != '') && (val.pieces != undefined && val.pieces != '')) {
        this.buspacklist[index].buscommercecargomixedlist[mixIndex].totalprice = Number(val.unitprice * val.pieces);
      }
    },
    //监听混装商品数量
    changeMixedPieces(val, index, mixIndex) {
      if ((val.unitprice != undefined && val.unitprice != '') && (val.pieces != undefined && val.pieces != '')) {
        this.buspacklist[index].buscommercecargomixedlist[mixIndex].totalprice = Number(val.unitprice * val.pieces);
      }
    },
  },
  created() {
    this.queryBuspacklist();
    this.selectCommon();
  }
}
</script>

<style scoped>

.ufms-black {
  background-color: #eee;
}

.ufms-white {
  background-color: #fafafa;
}

.title-font {
  text-align: start;
  font-size: 12px;
  padding: 2px 0px;
  font-weight: 600;
}

.el-button--mini, .el-button--mini.is-round {
  padding: 4px 10px;
}

.el-form-item__content {
  padding-bottom: 10px;
  border-top: 1px solid #e5e5e5;
  border-bottom: 1px solid #e5e5e5;
}

</style>