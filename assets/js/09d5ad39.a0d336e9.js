"use strict";(self.webpackChunkdocs=self.webpackChunkdocs||[]).push([[864],{5680:(e,n,t)=>{t.d(n,{xA:()=>c,yg:()=>m});var o=t(6540);function i(e,n,t){return n in e?Object.defineProperty(e,n,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[n]=t,e}function r(e,n){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);n&&(o=o.filter((function(n){return Object.getOwnPropertyDescriptor(e,n).enumerable}))),t.push.apply(t,o)}return t}function a(e){for(var n=1;n<arguments.length;n++){var t=null!=arguments[n]?arguments[n]:{};n%2?r(Object(t),!0).forEach((function(n){i(e,n,t[n])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):r(Object(t)).forEach((function(n){Object.defineProperty(e,n,Object.getOwnPropertyDescriptor(t,n))}))}return e}function l(e,n){if(null==e)return{};var t,o,i=function(e,n){if(null==e)return{};var t,o,i={},r=Object.keys(e);for(o=0;o<r.length;o++)t=r[o],n.indexOf(t)>=0||(i[t]=e[t]);return i}(e,n);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(o=0;o<r.length;o++)t=r[o],n.indexOf(t)>=0||Object.prototype.propertyIsEnumerable.call(e,t)&&(i[t]=e[t])}return i}var s=o.createContext({}),p=function(e){var n=o.useContext(s),t=n;return e&&(t="function"==typeof e?e(n):a(a({},n),e)),t},c=function(e){var n=p(e.components);return o.createElement(s.Provider,{value:n},e.children)},g="mdxType",d={inlineCode:"code",wrapper:function(e){var n=e.children;return o.createElement(o.Fragment,{},n)}},u=o.forwardRef((function(e,n){var t=e.components,i=e.mdxType,r=e.originalType,s=e.parentName,c=l(e,["components","mdxType","originalType","parentName"]),g=p(t),u=i,m=g["".concat(s,".").concat(u)]||g[u]||d[u]||r;return t?o.createElement(m,a(a({ref:n},c),{},{components:t})):o.createElement(m,a({ref:n},c))}));function m(e,n){var t=arguments,i=n&&n.mdxType;if("string"==typeof e||i){var r=t.length,a=new Array(r);a[0]=u;var l={};for(var s in n)hasOwnProperty.call(n,s)&&(l[s]=n[s]);l.originalType=e,l[g]="string"==typeof e?e:i,a[1]=l;for(var p=2;p<r;p++)a[p]=t[p];return o.createElement.apply(null,a)}return o.createElement.apply(null,t)}u.displayName="MDXCreateElement"},8971:(e,n,t)=>{t.r(n),t.d(n,{assets:()=>s,contentTitle:()=>a,default:()=>d,frontMatter:()=>r,metadata:()=>l,toc:()=>p});var o=t(8168),i=(t(6540),t(5680));const r={title:"4. Examples",sidebar_position:4},a="Examples",l={unversionedId:"examples",id:"examples",title:"4. Examples",description:"This section provides examples of how to implement the adidas MVI library in your Android application.",source:"@site/docs/examples.md",sourceDirName:".",slug:"/examples",permalink:"/mvi/docs/examples",draft:!1,editUrl:"https://github.com/facebook/docusaurus/tree/main/packages/create-docusaurus/templates/shared/docs/examples.md",tags:[],version:"current",sidebarPosition:4,frontMatter:{title:"4. Examples",sidebar_position:4},sidebar:"tutorialSidebar",previous:{title:"3. Getting started",permalink:"/mvi/docs/getting_started"}},s={},p=[{value:"1. Login Flow Example",id:"1-login-flow-example",level:2},{value:"ViewModel Implementation",id:"viewmodel-implementation",level:3}],c={toc:p},g="wrapper";function d(e){let{components:n,...t}=e;return(0,i.yg)(g,(0,o.A)({},c,t,{components:n,mdxType:"MDXLayout"}),(0,i.yg)("h1",{id:"examples"},"Examples"),(0,i.yg)("p",null,"This section provides examples of how to implement the adidas MVI library in your Android application.\nFor more detail check the sample provided."),(0,i.yg)("h2",{id:"1-login-flow-example"},"1. Login Flow Example"),(0,i.yg)("p",null,"Here is a complete example of how to implement a login flow using the adidas MVI library."),(0,i.yg)("h3",{id:"viewmodel-implementation"},"ViewModel Implementation"),(0,i.yg)("p",null,"The following code shows the ",(0,i.yg)("inlineCode",{parentName:"p"},"LoginViewModel"),", which handles the login process."),(0,i.yg)("pre",null,(0,i.yg)("code",{parentName:"pre",className:"language-kotlin"},"class LoginViewModel(\n    logger: Logger,\n    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default\n) : ViewModel(), MviHost<LoginIntent, State<LoginState, LoginSideEffect>> {\n\n    private val reducer = Reducer(\n        coroutineScope = viewModelScope,\n        defaultDispatcher = coroutineDispatcher,\n        initialInnerState = LoginState.LoggedOut(isLoggingIn = false),\n        logger = logger,\n        intentExecutor = this::executeIntent,\n    )\n\n    override val state = reducer.state\n\n    override fun execute(intent: LoginIntent) {\n        reducer.executeIntent(intent)\n    }\n\n    private fun executeIntent(intent: LoginIntent) =\n        when (intent) {\n            is LoginIntent.Login -> executeLogin(intent)\n            LoginIntent.Logout -> executeLogout()\n            LoginIntent.Close -> executeClose()\n        }\n\n    private fun executeLogin(intent: LoginIntent.Login) = flow {\n        emit(LoginTransform.SetIsLoggingIn(isLoggingIn = true))\n        delay(300) // Simulate a network call\n        emit(LoginTransform.SetIsLoggingIn(isLoggingIn = false))\n\n        if (intent.username.isEmpty() || intent.password.isEmpty()) {\n            emit(LoginTransform.AddSideEffect(LoginSideEffect.ShowInvalidCredentialsError))\n        } else {\n            emit(LoginTransform.SetLoggedIn(intent.username))\n        }\n    }\n}\n")),(0,i.yg)("h1",{id:"intent-example"},"Intent Example"),(0,i.yg)("p",null,"Define the intents that your application will handle:"),(0,i.yg)("pre",null,(0,i.yg)("code",{parentName:"pre",className:"language-kotlin"},"internal sealed class LoginIntent : Intent {\n    data class Login(val username: String, val password: String) : LoginIntent()\n    object Logout : LoginIntent()\n    object Close : LoginIntent()\n}\n")),(0,i.yg)("h1",{id:"activity-implementation"},"Activity Implementation"),(0,i.yg)("p",null,"Here's how to set up the ",(0,i.yg)("inlineCode",{parentName:"p"},"MviSampleActivity")," to use the ",(0,i.yg)("inlineCode",{parentName:"p"},"LoginViewModel"),"."),(0,i.yg)("pre",null,(0,i.yg)("code",{parentName:"pre",className:"language-kotlin"},"class MviSampleActivity : AppCompatActivity() {\n    private val viewModel: LoginViewModel by viewModel() // Assuming you're using Koin for dependency injection\n\n    override fun onCreate(savedInstanceState: Bundle?) {\n        super.onCreate(savedInstanceState)\n        setContent {\n            LoginScreen(viewModel)\n        }\n    }\n}\n")),(0,i.yg)("h1",{id:"view-implementation"},"View Implementation"),(0,i.yg)("p",null,"In your composable function, observe the state from the ",(0,i.yg)("inlineCode",{parentName:"p"},"LoginViewModel")," and render the UI accordingly."),(0,i.yg)("pre",null,(0,i.yg)("code",{parentName:"pre",className:"language-kotlin"},"@Composable\nfun LoginScreen(viewModel: LoginViewModel) {\n    val state by viewModel.state.collectAsState()\n\n    when (state) {\n        is LoginState.LoggedOut -> {\n            // Show login UI\n        }\n        is LoginState.LoggedIn -> {\n            // Show logged-in UI\n        }\n    }\n}\n")))}d.isMDXComponent=!0}}]);