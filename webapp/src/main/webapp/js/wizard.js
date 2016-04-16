Ext.onReady(function () {
    Ext.QuickTips.init();
    Ext.BLANK_IMAGE_URL = '../js/ext/resources/images/default/s.gif';
    Ext.form.Field.prototype.msgTarget = 'side';
    var cards = new Array();
    /************************************card0*****************************************************************************************/
    var card0 = new Ext.ux.Wiz.Card({
        title:'·系统部署',
        monitorValid:true,
        items:[
            {
                border:false,
                bodyStyle:'background:none;',
                html:'<p align="center" style="font-size:15;font-family:楷体_GB2312;font-weight:bolder;">欢迎来到系统部署界面</p><br/> '
            },
            {
                xtype:'radiogroup',
                items:[
                    {id:'deploy_1', boxLabel:'开始部署', name:'deploy_start', inputValue:1, checked:true}
                ]
            },
            {border:false, buttons:[

            ]}
        ]
    });
    /************************************card0*****************************************************************************************/
    /************************************card1*****************************************************************************************/
    var card1 = new Ext.ux.Wiz.Card({
        title:'部署流程·许可协议',
        monitorValid:true,
        labelAlign:'right',
        labelWidth:120,
        items:[
            {
                autoScroll:true,
                height:200,
                border:false,
                bodyStyle:'background:none;',
                html:'<p align="center" style="font-size:15;font-family:楷体_GB2312;font-weight:bolder;">用户使用许可协议</p><br/> ' +
                    '<p align="center" style="font-size:15;font-family:楷体_GB2312;"><br/>' +
                    '<p>一、软件使用协议</br></p>' +
                    '<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本协议是用户 (自然人、法人或社会团体)与本公司之间关于"CA"软件产品(以下简称"CA软件产品")的法律协议。一旦安装、复制或以其他方式使用CA软件产品,即表示同意接受协议各项条件的约束。如果用户不同意协议的条件,请不要使用CA软件产品。</p></br>' +
                    '<p>二、软件产品保护条款</p></br>' +
                    '<p>(1).CA软件产品之著作权及其它知识产权等相关权利或利益(包括但不限于现已取得或未来可取得之著作权、专利权、商标权、营业秘密等)皆为本公司所有。CA软件产品受中华人民共和国版权法及国际版权条约和其他知识产权法及条约的保护。用户仅获得CA软件产品的非排他性使用权。</p>' +
                    '<p>(2).用户不得：删除CA软件及其他副本上一切关于版权的信息；对CA软件进行反向工程,如反汇编、反编译等; </br>' +
                    '<p>(3).CA软件产品以现状方式提供,本公司不保证CA软件产品能够或不能够完全满足用户需求,在用户手册、帮助文件、使用说明书等软件文档中的介绍性内容仅供用户参考,不得理解为对用户所做的任何承诺。本公司保留对软件版本进行升级,对功能、内容、结构、界面、运行方式等进行修改或自动更新的权利。</p>' +
                    '<p>(4).为了更好地服务于用户,或为了向用户提供具有个性的信息内容的需要,CA软件产品可能会收集、传播某些信息,但本公司承诺不向未经授权的第三方提供此类信息,以保护用户隐私。</p>' +
                    '<p>(5).使用CA软件产品由用户自己承担风险,在适用法律允许的最大范围内,本公司在任何情况下不就因使用或不能使用CA软件产品所发生的特殊的、意外的、非直接或间接的损失承担赔偿责任。即使已事先被告知该损害发生的可能性。 </p>' +
                    '<p>(6).本公司定义的信息内容包括：文字、软件、声音；本公司为用户提供的商业信息,所有这些内容受版权、商标权、和其它知识产权和所有权法律的保护。所以,用户只能在本公司授权下才能使用这些内容,而不能擅自复制、修改、编撰这些内容、或创造与内容有关的衍生产品。</p>' +
                    '<p>(7).如果您未遵守本协议的任何一项条款,本公司有权立即终止本协议,并保留通过法律手段追究责任。</p></br>' +
                    '<p>三、软件解释权条款</br></p>' +
                    '<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本公司具有对以上各项条款内容的最终解释权和修改权。如用户对本公司的解释或修改有异议,应当立即停止使用CA软件产品。用户继续使用CA软件产品的行为将被视为对本公司的解释或修改的接受。</p></br>' +
                    '<p>四、软件纠纷条款</br></p>' +
                    '<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;因本协议所发生的纠纷,双方同意按照中华人民共和国法律,由本公司所在地的有管辖权的法院管辖。</p></br>' +
                    '</p>'
            },
            {
                xtype:'checkbox',
                fieldLabel:'同意',
                allowBlank:false,
                anchor:'95%',
                id:'deploy_access',
                checked:true,
                listeners:{
                    check:function (obj, ischecked) {
                        if (ischecked) {
                            wizard.nextButton.setDisabled(false);
                        } else {
                            wizard.nextButton.setDisabled(true);
                        }
                    }
                }
            }
        ]
    })
    /************************************card1*****************************************************************************************/
    /************************************card2*****************************************************************************************/
    var card2 = new Ext.ux.Wiz.Card({
        title:'系统部署·LDAP发布参数',
        labelWidth:120,
        monitorValid:true,
        labelAlign:'right',
        items:[
            {
                xtype:'fieldset',
                autoScroll:true,
                title:'LDAP发布参数',
                border:false,
                items:[
                    {
                        xtype:'textfield',
                        hidden:true,
                        allowBlank:false,
                        id:'ldap_hidden'
                    },
                    {
                        xtype:'checkbox',
                        fieldLabel:'启用内置LDAP',
                        allowBlank:false,
                        anchor:'95%',
                        id:'check_allowLDAP',
                        checked:true,
                        listeners:{
                            check:function (obj, ischecked) {
                                if (ischecked) {
                                    var LDAP_ip = Ext.getCmp('LDAP_ip');
                                    LDAP_ip.reset();
//                                    LDAP_ip.setDisabled(true);
                                    var LDAP_port = Ext.getCmp('LDAP_port');
                                    LDAP_port.reset();
//                                    LDAP_port.setDisabled(true);
                                    var LDAP_baseDN = Ext.getCmp('LDAP_baseDN');
                                    LDAP_baseDN.reset();
//                                    LDAP_baseDN.setDisabled(true);
                                    var LDAP_loginDN = Ext.getCmp('LDAP_loginDN');
                                    LDAP_loginDN.reset();
//                                    LDAP_loginDN.setDisabled(true);
                                    var LDAP_loginPASS = Ext.getCmp('LDAP_loginPASS');
                                    LDAP_loginPASS.reset();
//                                    LDAP_loginPASS.setDisabled(true);
//                                var LDAP_loginNUM = Ext.getCmp('LDAP_loginNUM');
//                                LDAP_loginNUM.reset();
//                                LDAP_loginNUM.setDisabled(true);
                                } else {
                                    Ext.getCmp('LDAP_ip').setDisabled(false);
                                    Ext.getCmp('LDAP_port').setDisabled(false);
                                    Ext.getCmp('LDAP_baseDN').setDisabled(false);
                                    Ext.getCmp('LDAP_loginDN').setDisabled(false);
                                    Ext.getCmp('LDAP_loginPASS').setDisabled(false);
//                                Ext.getCmp('LDAP_loginNUM').setDisabled(false);
                                }
                            }/*,*/
//                            render:function () {
//                                Ext.getCmp('check_allowLDAP').setDisabled(true);
//                                Ext.getCmp('LDAP_ip').setDisabled(true);
//                                Ext.getCmp('LDAP_port').setDisabled(true);
//                                Ext.getCmp('LDAP_baseDN').setDisabled(true);
//                                Ext.getCmp('LDAP_loginDN').setDisabled(true);
//                                Ext.getCmp('LDAP_loginPASS').setDisabled(true);
//                            Ext.getCmp('LDAP_loginNUM').setDisabled(true);
//                            }
                        }
                    },
                    {
                        xtype:'textfield',
                        fieldLabel:'LDAP服务器IP',
                        value:'127.0.0.1',
                        allowBlank:false,
                        anchor:'95%',
                        id:'LDAP_ip'
                    } ,
                    {
                        xtype:'textfield',
                        fieldLabel:'LDAP服务器端口',
                        allowBlank:false,
                        value:389,
                        anchor:'95%',
                        id:'LDAP_port'
                    },
                    {
                        xtype:'textfield',
                        fieldLabel:'发布DN',
                        value:'dc=pkica',
                        allowBlank:false,
                        anchor:'95%',
                        id:'LDAP_baseDN'
                    } ,
                    {
                        xtype:'textfield',
                        fieldLabel:'登陆DN',
                        value:"cn=admin,dc=pkica",
                        allowBlank:false,
                        anchor:'95%',
                        id:'LDAP_loginDN'
                    } ,
                    {
                        xtype:'textfield',
                        fieldLabel:'登陆密码',
                        value:'123456',
                        inputType:'password',
                        allowBlank:false,
                        anchor:'95%',
                        id:'LDAP_loginPASS'
                    }/* ,{
                     xtype: 'textfield',
                     fieldLabel: 'LDAP最大连接数',
                     allowBlank: false,
                     anchor: '95%',
                     value:5,
                     id: 'LDAP_loginNUM'
                     }*/
                ], buttons:['->', {
                text:'LDAP连通性校验',
                handler:function () {
                    Ext.getCmp("ldap_hidden").reset();
                    var LDAP_ip = Ext.getCmp("LDAP_ip").getValue();
                    var LDAP_port = Ext.getCmp("LDAP_port").getValue();
                    var LDAP_baseDN = Ext.getCmp("LDAP_baseDN").getValue();
                    var LDAP_loginDN = Ext.getCmp("LDAP_loginDN").getValue();
                    var LDAP_loginPASS = Ext.getCmp("LDAP_loginPASS").getValue();
                    Ext.Ajax.request({
                        url:'LdapConfigAction_ldapConnections.action',
                        params:{host:LDAP_ip, port:LDAP_port, base:LDAP_baseDN, adm:LDAP_loginDN, pwd:LDAP_loginPASS},
                        method:'POST',
                        success:function (r, o) {
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            if (respText.success == true) {
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    buttons: Ext.MessageBox.OK,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false
                                });
                                Ext.getCmp("ldap_hidden").setValue('hidden');
                            } else {
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    buttons: Ext.MessageBox.OK,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false
                                });
                            }
                        },
                        failure:function (r, o) {
                            var respText = Ext.util.JSON.decode(r.responseText);
                            var msg = respText.msg;
                            Ext.MessageBox.show({
                                title: '信息',
                                width: 250,
                                msg: msg,
                                buttons: Ext.MessageBox.OK,
                                buttons: {'ok': '确定'},
                                icon: Ext.MessageBox.INFO,
                                closable: false
                            });
                        }
                    });
                }
            }]}

        ]
    })
    /************************************card2*****************************************************************************************/
   /************************************card3*****************************************************************************************/
    var province_store = new Ext.data.Store({
        reader: new Ext.data.JsonReader({
            fields: ["id", "districtName"],
            totalProperty: 'totalCount',
            root: 'root'
        })
    });

    var keyBits = [
        ['1024', '1024 bits'],
        ['2048', '2048 bits'],
        ['4096', '4096 bits']
    ];

    var card3 = new Ext.ux.Wiz.Card({
        title:'部署流程·签发CA',
        labelWidth:80,
        monitorValid:true,
        labelAlign:'right',
        autoScroll:true,
        buttonAlign:'center',
        items:[
            {
                xtype:'fieldset',
                autoScroll:true,
                title:'基本信息',
                buttonAlign:'center', //居中
                border:false,
                items:[
                    {
                        xtype:'textfield',
                        hidden:true,
                        allowBlank:false,
                        id:'userCA_hidden'
                    },
                    {
                        xtype:'textfield',
                        fieldLabel:'通用名',
                        value:'CA',
//                        readOnly:true,
                        allowBlank:false,
                        anchor:'95%',
                        name:'cn',
                        id:'userCA_cn'
                    },
                    new Ext.form.ComboBox({
                        mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
                        border: true,
                        frame: true,
                        anchor:'95%',
                        pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
                        // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
                        editable: false,
                        fieldLabel: '省/行政区',
                        emptyText: '请选择所在省/行政区',
                        id: 'userCA_province',
//                hiddenName : 'x509Ca.province',
                        triggerAction: "all",// 是否开启自动查询功能
                        store: province_store,// 定义数据源
                        valueField: "districtName", // 关联某一个逻辑列名作为显示值
                        displayField: "districtName", // 关联某一个逻辑列名作为显示值
//                valueField: "id", // 关联某一个逻辑列名作为实际值
                        //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                        allowBlank: false,
                        blankText: "请选择所在省/行政区",
                        listeners: {
                            /* select: function () {
                             var value = this.getValue();
                             city_store.proxy = new Ext.data.HttpProxy({
                             url: "../DistrictAction_findCity.action?parentId=" + value
                             })
                             city_store.load();
                             },*/
                            render: function () {
                                province_store.proxy = new Ext.data.HttpProxy({
                                    url: '../DistrictAction_findProvince.action',
                                    method: "POST"
                                })
                            }
                        }
                    }),
                    /*new Ext.form.ComboBox({
                     mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
                     border: true,
                     frame: true,
                     pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
                     // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
                     editable: false,
                     fieldLabel: '城市/乡镇',
                     emptyText: '请选择所在城市/乡镇',
                     id: 'x509Ca.msg.city',
                     //                hiddenName: 'x509Ca.orgCode',
                     triggerAction: "all",// 是否开启自动查询功能
                     store: city_store,// 定义数据源
                     displayField: "districtName", // 关联某一个逻辑列名作为显示值
                     valueField: "id", // 关联某一个逻辑列名作为实际值
                     //mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                     name: 'x509Ca.city',
                     //                hiddenName: 'x509Ca.city',
                     allowBlank: false,
                     blankText: "请选择所在城市/乡镇"
                     }),*/
                    new Ext.form.TextField({
                        fieldLabel: '城市/乡镇',
                        anchor:'95%',
                        emptyText:"请输入所在城市/乡镇",
                        regex: /^[a-zA-Z0-9\u4e00-\u9fa5]+$/,
                        regexText: '只能输入数字,字母,中文!',
                        id: 'userCA_city',
                        allowBlank: false,
                        blankText: "不能为空，请正确填写所在城市/乡镇"
                    }),
                    new Ext.form.ComboBox({
                        fieldLabel:'密钥位数',
                        emptyText:'请选择密钥位数',
                        anchor:'95%',
                        typeAhead:true,
                        triggerAction:'all',
                        forceSelection:true,
                        id:"userCA_keyLength",
                        mode:'local',
                        hiddenName:"x509Ca.keyLength",
                        store:new Ext.data.ArrayStore({
                            fields:[
                                'id',
                                'name'
                            ],
                            data:keyBits
                        }),
                        valueField:'id', //下拉框具体的值（例如值为SM，则显示的内容即为‘短信’）
                        displayField:'name'//下拉框显示内容
                    }),
                    {
                        xtype:'textfield',
                        fieldLabel:'有效期(天)',
                        allowBlank:false,
                        enable:false,
                        value:3650,
                        anchor:'95%',
                        id:'userCA_validaty',
                        listeners:{
                            render:function () {
                                Ext.getCmp('userCA_validaty').setDisabled(true);
                            }
                        }
                    }
                    /* {
                     xtype:'fieldset',
                     anchor: '95%',
                     //                        title:'时间范围',
                     items:[{
                     plain:true,
                     border:false,
                     layout: 'form',
                     items:[{
                     plain:true,
                     labelWidth:0,
                     labelAlign:'right',
                     defaultType:'textfield',
                     border:false,
                     layout: 'form',
                     defaults:{
                     allowBlank:false,
                     blankText:'该项不能为空！'
                     },
                     items:[{
                     fieldLabel:'有效期',
                     name:'account.title',
                     regex:/^.{2,30}$/,
                     anchor: '95%',
                     regexText:'请输入任意2--30个字符',
                     emptyText:'请输入任意2--30个字符'
                     },{
                     fieldLabel:'单位',
                     xtype:'combo',
                     anchor: '95%',
                     typeAhead: true,
                     triggerAction: 'all',
                     forceSelection: true,
                     mode: 'local',
                     hiddenName:"day",
                     store: dayBits,
                     valueField: 'id',   //下拉框具体的值（例如值为SM，则显示的内容即为‘短信’）
                     displayField: 'name'//下拉框显示内容
                     }]
                     }]
                     }]
                     }*/
                ], buttons:['->',
                {
                    xtype:'button',
                    text:'签发证书',
                    id:'sign_localCA',
                    handler:function () {
                        var cn = Ext.getCmp('userCA_cn').getValue();
                        var province = Ext.getCmp('userCA_province').getValue();
                        var city = Ext.getCmp("userCA_city").getValue();
                        var bit = Ext.getCmp("userCA_keyLength").getValue();
                        var validaty = Ext.getCmp('userCA_validaty').getValue();
                        var base = Ext.getCmp("LDAP_baseDN").getValue();
                        var host = Ext.getCmp("LDAP_ip").getValue();
                        var port = Ext.getCmp("LDAP_port").getValue();
                        var adm = Ext.getCmp("LDAP_loginDN").getValue();
                        var pwd = Ext.getCmp("LDAP_loginPASS").getValue();
                        Ext.getCmp('userCA_hidden').reset();
                        Ext.MessageBox.show({
                            title:"信息",
                            width:250,
                            msg:"确定信息无误,确定后将签发CA证书?",
                            icon:Ext.MessageBox.WARNING,
                            buttons:{'ok':'确定', 'no':'取消'},
                            fn:function (e) {
                                if (e == 'ok') {
                                    var myMask = new Ext.LoadMask(Ext.getBody(), {
                                        msg:'签发中,请等待...',
                                        removeMask:true //完成后移除
                                    });
                                    myMask.show();
                                    Ext.Ajax.request({
                                        url:'X509CaAction_selfSign.action',
                                        params:{cn:cn, province:province,city:city,bit:bit,
                                            validaty:validaty, base:base,host:host,port:port,adm:adm,pwd:pwd},
                                        method:'POST',
                                        success:function (r, o) {
                                            myMask.hide();
                                            var respText = Ext.util.JSON.decode(r.responseText);
                                            var msg = respText.msg;
                                            if (respText.success == true) {//如果你处理的JSON串中true不是字符串，就obj.success == true
                                                //你后台返回success 为 false时执行的代码
                                                Ext.MessageBox.show({
                                                    title: '信息',
                                                    width: 250,
                                                    msg: msg,
                                                    buttons: Ext.MessageBox.OK,
                                                    buttons: {'ok': '确定'},
                                                    icon: Ext.MessageBox.INFO,
                                                    closable: false
                                                });
                                                Ext.getCmp('userCA_hidden').setValue('hidden');
                                            } else {
                                                //你后台返回success 为 false时执行的代码
                                                Ext.MessageBox.show({
                                                    title: '信息',
                                                    width: 250,
                                                    msg: msg,
                                                    buttons: Ext.MessageBox.OK,
                                                    buttons: {'ok': '确定'},
                                                    icon: Ext.MessageBox.INFO,
                                                    closable: false
                                                });
                                                //你后台返回success 为 false时执行的代码
                                            }
                                        },
                                        failure:function (r, o) {
                                            myMask.hide();
                                            var respText = Ext.util.JSON.decode(r.responseText);
                                            var msg = respText.msg;
                                            Ext.MessageBox.show({
                                                title: '信息',
                                                width: 250,
                                                msg: msg,
                                                buttons: Ext.MessageBox.OK,
                                                buttons: {'ok': '确定'},
                                                icon: Ext.MessageBox.INFO,
                                                closable: false
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    }
                }/*, {
                 xtype: 'button',
                 text:'签发分发式CA',
                 id: 'sign_splitCA'
                 }*/
            ]
            }
        ]
    })
    /************************************card3*****************************************************************************************/
    /************************************card4*****************************************************************************************/
    var card4 = new Ext.ux.Wiz.Card({
        title:'部署流程·完成部署',
        labelWidth:120,
        labelAlign:'right',
        monitorValid:true,
        items:[
            {
                xtype:'fieldset',
                autoScroll:true,
                title:'完成部署',
                border:false,
                buttonAlign:'right',
//            border: true,
                items:[

                    {
                        border:false,
                        bodyStyle:'background:none;',
                        html:'<p style="text-align: center;" >部署已经完成,请点击"完成"按钮.确认部署完成!</p><br/> '
                    }
                ]/*,buttons:[
             {
             xtype: 'button',
             text: '<p align="center"  style="color: #0000ff;">部署完成</p>',
             id: 'end_deploy'
             }]*/}
        ]
    })
    /************************************card4*****************************************************************************************/

    cards.push(card0);
    cards.push(card1);
    cards.push(card2);
    cards.push(card3);
    cards.push(card4);
    //部署主界面
    var wizard = new Ext.ux.Wiz({
        headerConfig:{
            title:"<font style=\"font-family:楷体_GB2312;font-size:15px;\">部署流程</font>"
        },
        cardPanelConfig:{
            defaults:{
                baseCls:'x-small-editor',
                bodyStyle:'padding:40px 15px 5px 120px;background-color:#212936;',
                border:false
            }
        },
        cards:cards,
        listeners:{
            'finish':function () {
                var host = Ext.getCmp("LDAP_ip").getValue();
                var port = Ext.getCmp("LDAP_port").getValue();
                var base = Ext.getCmp("LDAP_baseDN").getValue();
                var adm = Ext.getCmp("LDAP_loginDN").getValue();
                var pwd = Ext.getCmp("LDAP_loginPASS").getValue();
                Ext.Ajax.request({
                    url:'WizardAction_finish.action',
                    method:'POST',
                    params:{
                        host:host,
                        port:port,
                        base:base,
                        adm:adm,
                        pwd:pwd
                    },
                    success:function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        if (respText.success == true) {
                            Ext.Msg.show({
                                title:'信息',
                                msg:msg,
                                buttons:Ext.Msg.OK,
                                fn:function () {
                                    Ext.Ajax.request({
                                        url: 'DeployAction_upgradeService.action',
                                        timeout:20 * 60 * 1000,
                                        method:'POST',
                                        success:function (r, o) {
                                            Ext.MessageBox.show({
                                                title: '信息',
                                                width: 250,
                                                msg: "服务重启中........!",
                                                buttons: Ext.MessageBox.OK,
                                                buttons: {'ok': '确定'},
                                                icon: Ext.MessageBox.INFO,
                                                closable: false
                                            });
                                        },
                                        failure:function (r, o) {
                                            Ext.MessageBox.show({
                                                title: '信息',
                                                width: 250,
                                                msg: "服务未正常重启!",
                                                buttons: Ext.MessageBox.OK,
                                                buttons: {'ok': '确定'},
                                                icon: Ext.MessageBox.INFO,
                                                closable: false
                                            });
                                        }
                                    });
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title: '信息',
                                width: 250,
                                msg: msg,
                                buttons: Ext.MessageBox.OK,
                                buttons: {'ok': '关闭'},
                                icon: Ext.MessageBox.INFO,
                                closable: false
                            });
                        }
                    }
                });
            }
        }
    });

    // 显示部署界面
    wizard.show();
});










