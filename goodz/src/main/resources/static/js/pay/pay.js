const button = document.getElementById("payment-button");
        
        const generateRandomString = () => window.btoa(Math.random()).slice(0, 20);
        var amount = 50000;
        // ------  결제위젯 초기화 ------
        // TODO: clientKey는 개발자센터의 결제위젯 연동 키 > 클라이언트 키로 바꾸세요.
        // TODO: 구매자의 고유 아이디를 불러와서 customerKey로 설정하세요. 이메일・전화번호와 같이 유추가 가능한 값은 안전하지 않습니다.
        // @docs https://docs.tosspayments.com/reference/widget-sdk#sdk-설치-및-초기화
        const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";
        const customerKey = generateRandomString();
        const paymentWidget = PaymentWidget(clientKey, customerKey); // 회원 결제
        // const paymentWidget = PaymentWidget(clientKey, PaymentWidget.ANONYMOUS); // 비회원 결제
  
        // ------  결제 UI 렌더링 ------
        // @docs https://docs.tosspayments.com/reference/widget-sdk#renderpaymentmethods선택자-결제-금액-옵션
        paymentMethodWidget = paymentWidget.renderPaymentMethods(
          "#payment-method",
          { value: amount },
          // 렌더링하고 싶은 결제 UI의 variantKey
          // 결제 수단 및 스타일이 다른 멀티 UI를 직접 만들고 싶다면 계약이 필요해요.
          // @docs https://docs.tosspayments.com/guides/payment-widget/admin#멀티-결제-ui
          { variantKey: "DEFAULT" }
        );
        // ------  이용약관 UI 렌더링 ------
        // @docs https://docs.tosspayments.com/reference/widget-sdk#renderagreement선택자-옵션
        //paymentWidget.renderAgreement("#agreement", { variantKey: "AGREEMENT" });
  
        //  ------  결제 UI 렌더링 완료 이벤트 ------
        paymentMethodWidget.on("ready", function () {
          button.disabled = false;
          coupon.disabled = false;
        });
  
        // ------  결제 금액 업데이트 ------
        // @docs https://docs.tosspayments.com/reference/widget-sdk#updateamount결제-금액
        // coupon.addEventListener("change", function () {
        //   if (coupon.checked) {
        //     paymentMethodWidget.updateAmount(amount - 5000);
        //   } else {
        //     paymentMethodWidget.updateAmount(amount);
        //   }
        // });
  
        // ------ '결제하기' 버튼 누르면 결제창 띄우기 ------
        // @docs https://docs.tosspayments.com/reference/widget-sdk#requestpayment결제-정보
        button.addEventListener("click", function () {
          // 결제를 요청하기 전에 orderId, amount를 서버에 저장하세요.
          // 결제 과정에서 악의적으로 결제 금액이 바뀌는 것을 확인하는 용도입니다.
          paymentWidget.requestPayment({
            orderId: generateRandomString(),
            orderName: "토스 티셔츠 외 2건",
            successUrl: window.location.origin + "/success",
            failUrl: window.location.origin + "/fail",
            customerEmail: "customer123@gmail.com",
            customerName: "김토스",
            customerMobilePhone: "01012341234",
          });
        });