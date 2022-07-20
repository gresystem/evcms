package kr.co.watchpoint.evcms.constants;

public class EventStatus {
	/* 카테고리
	 * 0: 유저
	 * 1: 충전기
	 * 2: 비회원
	 * */
	public final static String EVT_CATT_USER = "0";
	public final static String EVT_CATT_MACHINE = "1";
	public final static String EVT_CATT_UNREGISTERED = "2";

	
	/* 타입
	 * 0: 일반
	 * 1: 경고
	 * 2: 위험
	 * */
	public final static String EVT_TYPE_NORMAL = "0";
	public final static String EVT_TYPE_WARNING = "1";
	public final static String EVT_TYPE_DANGER = "2";

	/* 유저 서브타입
	 * 0: 로그인 관련
	 * 1: 충전 관련
	 * 2: 회원카드 관련
	 * 3: 결제 관련
	 * 4: 상태 관련
	 * */
	public final static String EVT_SUBTYPE_USER_LOGIN = "0";
	public final static String EVT_SUBTYPE_USER_CHARGE = "1";
	public final static String EVT_SUBTYPE_USER_CARDTAG = "2";
	public final static String EVT_SUBTYPE_USER_PAYMENT = "3";
	public final static String EVT_SUBTYPE_USER_STATUS = "4";

	/* 충전기 서브타입
	 * 0: 충전 관련
	 * 1: 기기 관련
	 * 2: 통신 관련
	 * */
	public final static String EVT_SUBTYPE_MACHINE_CHARGE = "0";
	public final static String EVT_SUBTYPE_MACHINE_DEVICE = "1";
	public final static String EVT_SUBTYPE_MACHINE_COMMUNICATION = "2";
	
	/* 비회원 서브타입
	 * 0: 충전 관련
	 * 1: 결제 관련
	 * */
	public final static String EVT_SUBTYPE_UNREGISTERED_CHARGE = "3";
	public final static String EVT_SUBTYPE_UNREGISTERED_PAYMENT = "4";
}
