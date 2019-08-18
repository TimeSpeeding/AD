using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Group13SSIS.Models;
using PagedList;

namespace Group13SSIS.Controllers
{
    public class EmployeeController : Controller
    {
        private Cart GetCart()
        {
            Cart cart = (Cart)Session["Cart"];
            if (cart == null)
            {
                cart = new Cart();
                Session["Cart"] = cart;
            }
            return cart;
        }
        public ActionResult Index()
        {
            return View();
        }
        public ActionResult StationeryList(string Category, string Search, int? page)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var cateLst = db.Stationeries.Select(x => x.Category).Distinct().ToList();
                ViewBag.Category = new SelectList(cateLst);
                var stationeries = db.Stationeries.ToList();
                if (!String.IsNullOrEmpty(Category))
                {
                    stationeries = stationeries.Where(x => x.Category == Category).ToList();
                }
                if (!String.IsNullOrEmpty(Search))
                {
                    stationeries = stationeries.Where(s => s.Description.ToLower().Contains(Search.ToLower())).ToList();
                }
                const int pageItems = 20;
                int currentPage = (page ?? 1);
                IPagedList<Stationery> pageStationeries = stationeries.ToPagedList(currentPage, pageItems);
                StationeryVM stationeryVM = new StationeryVM()
                {
                    Stationeries = pageStationeries,
                    Category = Category,
                    Search = Search
                };
                return View(stationeryVM);
            }
        }
        public ActionResult AddToCart(int id, int? qty)
        {
            Cart cart = GetCart();
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                Stationery stationery = db.Stationeries.Where(x => x.StationeryId == id).FirstOrDefault();
                if (stationery != null && qty != null && qty > 0)
                {
                    cart.AddItem(stationery, qty ?? default(int));
                }
            }
            return RedirectToAction("StationeryList");
        }
        public ActionResult CartList()
        {
            Cart cart = GetCart();
            ViewData["cartlist"] = cart.GetList();
            return View();
        }
        [HttpPost]
        public ActionResult IncreaseOrDecreaseOne(int id, int quantity)
        {
            Cart cart = GetCart();
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                Stationery stationery = db.Stationeries.Where(x => x.StationeryId == id).FirstOrDefault();
                if (stationery != null)
                {
                    cart.IncreaseOrDecreaseOne(stationery, quantity);
                }
            }
            return Json(new { msg = true });
        }
        public ActionResult RemoveFromCart(int id)
        {
            Cart cart = GetCart();
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                Stationery stationery = db.Stationeries.Where(x => x.StationeryId == id).FirstOrDefault();
                if (stationery != null)
                {
                    cart.RemoveLine(stationery);
                }
            }
            return RedirectToAction("CartList");
        }
        public ActionResult CreateRequisition()
        {
            Cart cart = GetCart();
            User user = (User)Session["user"];
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                Requisition requisition = new Requisition();
                requisition.ApplicantId = user.UserId;
                requisition.Date = DateTime.Today;
                requisition.Status = "Pending";
                db.Requisitions.Add(requisition);
                db.SaveChanges();
            }
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                Requisition re = db.Requisitions.Where(x => x.ApplicantId == user.UserId && x.Status == "Pending").FirstOrDefault();
                foreach (var item in cart.GetList())
                {
                    RequisitionDetail requisitionDetail = new RequisitionDetail();
                    requisitionDetail.RequisitionId = re.RequisitionId;
                    requisitionDetail.StationeryId = item.Stationery.StationeryId;
                    requisitionDetail.Quantity = item.Qty;
                    db.RequisitionDetails.Add(requisitionDetail);
                }
                re.Status = "Applied";
                db.SaveChanges();
            }
            Session["cart"] = new Cart();
            return RedirectToAction("CartList");
        }
        public ActionResult RequisitionList()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                ViewData["requisitionlist"] = db.Requisitions.ToList();
            }
            return View();
        }
        public ActionResult RequisitionDetails(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                ViewData["requisition"] = db.Requisitions.Where(x => x.RequisitionId == id).FirstOrDefault();
                var details = db.RequisitionDetails.Where(x => x.RequisitionId == id).ToList();
                var list = new List<CartLine>();
                foreach (var item in details)
                {
                    CartLine cartLine = new CartLine()
                    {
                        Stationery = db.Stationeries.Where(x => x.StationeryId == item.StationeryId).FirstOrDefault(),
                        Qty = item.Quantity
                    };
                    list.Add(cartLine);
                }
                ViewData["list"] = list;
            }
            return View();
        }
    }
}