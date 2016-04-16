/**
 * Created by Administrator on 14-9-26.
 */

Ext.onReady(function() {

    Ext.BLANK_IMAGE_URL = '../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var record = new Ext.data.Record.create([
        {name:'wifi', mapping:'wifi'} ,
        {name:'sslvpn', mapping:'sslvpn'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../SystemStatusServlet?command=status"
    });

    var reader = new Ext.data.JsonReader({
        totalProperty:"totalCount",
        root:"root"
    }, record);

    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy:proxy,
        reader:reader
    });

    store.load();
    store.on('load',function(){
        var wifi = store.getAt(0).get('wifi');
        var sslvpn = store.getAt(0).get('sslvpn');
        Ext.getCmp('net.status').setValue(wifi);
        Ext.getCmp('sslvpn.status').setValue(sslvpn);
    });


    var start = 0;
    var pageSize = 15;
    var record = new Ext.data.Record.create([
        {name:'ip',			mapping:'ip'},
        {name:'mac',		mapping:'mac'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url:"../SystemStatusServlet?command=getWifiList"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty:"totalCount",
        root:"rows"
    },record);
    var store_grid = new Ext.data.GroupingStore({
        id:"store_grid.info",
        proxy : proxy,
        reader : reader
    });
    store_grid.load({
        params:{
            start:start,limit:pageSize
        }
    });
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        rowNumber,
        {header:'IP地址',		dataIndex:'ip',		   align:'center',sortable:true,menuDisabled:true},
        {header:"MAC地址",	dataIndex:"mac",       align:'center',sortable:true,menuDisabled:true}
    ]);
    var page_toolbar = new Ext.PagingToolbar({
        pageSize : pageSize,
        store:store_grid,
        displayInfo:true,
        displayMsg:"显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg:"没有记录",
        beforePageText:"当前页",
        afterPageText:"共{0}页"
    });



    var grid_panel = new Ext.grid.GridPanel({
        id:'grid.info',
        title:'列表',
        plain:true,
        autoHeight:true,
        animCollapse:true,
        loadMask:{msg:'正在加载数据，请稍后...'},
        border:true,
        collapsible:false,
        cm:colM,
        store:store_grid,
        stripeRows:true,
        autoExpandColumn:'Position',
        disableSelection:true,
        bodyStyle:'width:100%',
        enableDragDrop: true,
        selModel:new Ext.grid.RowSelectionModel({singleSelect:true}),
        viewConfig:{
            forceFit:true,
            enableRowBody:true,
            getRowClass:function(record,rowIndex,p,store){
                return 'x-grid3-row-collapsed';
            }
        },
        view:new Ext.grid.GroupingView({
            forceFit:true,
            groupingTextTpl:'{text}({[values.rs.length]}条记录)'
        }),
        bbar:page_toolbar
    });
    var formPanel = new Ext.form.FormPanel({
        plain:true,
        labelAlign:'right',
        labelWidth:150,
        defaultType:'textfield',
        defaults: {
             anchor : '100%',
            allowBlank:false,
            blankText:'该项不能为空!'
        },
        items:[
            {
                xtype: 'fieldset',
                title: '状态信息',
                defaultType: 'textfield',
                labelWidth: 150,
                defaults: {
                    anchor : '100%',
                    allowBlank: false,
                    blankText: '该项不能为空!'
                },
                items: [{
                    xtype: 'fieldset',
                    collapsible: true,
                    title: '网络连接状态',
                    defaultType: 'textfield',
                    labelWidth: 150,
                    defaults: {
                        anchor : '100%',
                        allowBlank: false,
                        blankText: '该项不能为空!'
                    },
                    items:[ {
                        fieldLabel:'数据状态',
                        id:'net.status',
                        name:'net_status',
                        xtype:'displayfield'
                    }]
                },{
                    xtype: 'fieldset',
                    collapsible: true,
                    title: 'SSLVPN连接状态',
                    defaultType: 'textfield',
                    labelWidth: 150,
                    defaults: {
                        anchor : '100%',
                        allowBlank: false,
                        blankText: '该项不能为空!'
                    },
                    items:[{
                        fieldLabel:'隧道状态',
                        id:'sslvpn.status',
                        name:'sslvpn_status',
                        xtype:'displayfield'
                    }]
                }]
            }
        ]
    });

    var panel_check = new Ext.Panel({
        frame:true,
        renderTo:"content",
        plain:true,
        border:true,
        height:setHeight(),
        autoScroll:true,
        items:[{
            xtype:'fieldset',
            title:'系统状态',
            items:[
                formPanel,
                {
                    xtype:'fieldset',
                    collapsible: true,
                    title:"WLAN连接用户列表",
                    labelWidth:150,
                    defaults: {
                        anchor : '100%',
                        allowBlank:false,
                        blankText:'该项不能为空!'
                    },
                    items:[grid_panel]
                }]
        }]
    });
});

function setHeight(){
    return document.getElementById("content").offsetHeight;
}