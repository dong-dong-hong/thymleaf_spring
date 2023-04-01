package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

//    @Autowired 생략 가능 하나기 때문
//    public BasicItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId,Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }
//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item",item);

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV2(
            @ModelAttribute("item") Item item
            ,Model model
    ) {

        itemRepository.save(item);
//        model.addAttribute("item",item); // 자동 추가, 생략 가능
        return "basic/item";

        // @ModelAttribute - 요청 파라미터 처리
        // @ModelAttribute 는 Item 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로 입력해준다.

        //@ModelAttribute - Model 추가
        //@ModelAttribute 는 중요한 한가지 기능이 더 있는데, 바로 모델(Model)에 @ModelAttribute 로 지정한 객체를 자동으로 넣어준다.
        // 지금 코드를 보면 model.addAttribute("item", item) 가 주석처리 되어 있어도 잘 동작하는 것을 확인할 수 있다.

        // 모델에 데이터를 담을 때는 이름이 필요하다.
        // 이름은 @ModelAttribute 에 지정한 name(value) 속성을 사용한다.
        // 만약 다음과 같이 @ModelAttribute 의 이름을 다르게 지정하면 다른 이름으로 모델에 포함된다.

        //@ModelAttribute("hello") Item item 이름을 hello 로 지정
        //model.addAttribute("hello", item); 모델에 hello 이름으로 저장
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item";
        // @ModelAttribute 의 이름을 생략하면 모델에 저장될 때 클래스명을 사용한다.
        // 이때 클래스의 첫글자만 소문자로 변경해서 등록한다.
        //예) @ModelAttribute 클래스명 모델에 자동 추가되는 이름
        //Item item
        //HelloWorld helloWorld
    }

//    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
        // @ModelAttribute 자체도 생략가능하다.
        // 대상 객체는 모델에 자동 등록된다. 나머지 사항은 기존과 동일하다.
    }

    /*
      PRG - Post/Redirect/Get
     */

//    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId() ;

        // 상품 등록 처리 이후에 뷰 템플릿이 아니라 상품 상세 화면으로 리다이렉트 하도록 코드를 작성해보자.
        // 이런 문제 해결 방식을 PRG Post/Redirect/Get 라 한다.

        //주의
        //> "redirect:/basic/items/" + item.getId() redirect에서 +item.getId() 처럼 URL에 변수를
        // 더해서 사용하는 것은 URL 인코딩이 안되기 때문에 위험하다.
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){
        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId" , saveItem.getId()); // ※ model.addAttribute : 템플릿 엔진에 자바 변수를 넘기는 메서드
        redirectAttributes.addAttribute("status" , true);
        return "redirect:/basic/items/{itemId}";

        // RedirectAttributes
        //RedirectAttributes 를 사용하면 URL 인코딩도 해주고, pathVarible , 쿼리 파라미터까지 처리해준다.
        //redirect:/basic/items/{itemId}
        //pathVariable 바인딩: {itemId} @pathVariable 어노테이션은 요청 URL 매핑에서 템플릿 변수를 처리하고 이를 메서드 매개변수로 설정하는 데 사용
        //나머지는 쿼리 파라미터로 처리: ?status=true
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId,@ModelAttribute Item item){
        itemRepository.update(itemId,item);
        return "redirect:/basic/items/{itemId}";
    }


    // 상품 수정은 상품 등록과 전체 프로세스가 유사하다.
    //GET /items/{itemId}/edit : 상품 수정 폼
    //POST /items/{itemId}/edit : 상품 수정 처리

    //리다이렉트
    //상품 수정은 마지막에 뷰 템플릿을 호출하는 대신에 상품 상세 화면으로 이동하도록 리다이렉트를 호출한다.
    //스프링은 redirect:/... 으로 편리하게 리다이렉트를 지원한다.
    //redirect:/basic/items/{itemId}
    //컨트롤러에 매핑된 @PathVariable 의 값은 redirect 에도 사용 할 수 있다.
    //redirect:/basic/items/{itemId} {itemId} 는 @PathVariable Long itemId 의 값을 그대로 사용한다.

    //> 참고
    //> HTML Form 전송은 PUT, PATCH를 지원하지 않는다. GET, POST만 사용할 수 있다.
    //> PUT, PATCH는 HTTP API 전송시에 사용
    //> 스프링에서 HTTP POST로 Form 요청할 때 히든 필드를 통해서 PUT, PATCH 매핑을 사용하는 방법이 있지만, HTTP 요청 상 POST 요청이다.

    // 테스트용 데이터 추가
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA",10000,10));
        itemRepository.save(new Item("itemB",20000,20));
    }
}
